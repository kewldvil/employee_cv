package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.dto.UserFileDTO;
import com.noc.employee_cv.model.FileUpload;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.FileUploadRepo;
import com.noc.employee_cv.repository.UserRepo;
import com.noc.employee_cv.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg",
            "image/png"
    );

    private final FileUploadRepo fileUploadRepo;
    private final Path fileStorageLocation;
    private final UserRepo userRepo;

    public FileServiceImpl(@Value("${file.upload-dir}") String uploadDir,
                          FileUploadRepo fileUploadRepo,
                          UserRepo userRepo) {
        this.fileUploadRepo = fileUploadRepo;
        this.userRepo = userRepo;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    @Override
    @Transactional
    public FileUpload uploadFile(MultipartFile fileUpload, User user) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename()));
        String fileName = generateUniqueFileName(originalFilename);

        try {
            validateUpload(fileUpload, fileName);
            FileUpload existingFile = fileUploadRepo.findByUserAndFileName(user, fileName);
            if (existingFile != null) {
                Path existingFilePath = resolveStoragePath(existingFile.getFileName());
                Files.deleteIfExists(existingFilePath);
                fileUploadRepo.delete(existingFile);
            }

            Path targetLocation = resolveStoragePath(fileName);
            Files.copy(fileUpload.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileUpload file = new FileUpload();
            file.setUser(user);
            file.setFileName(fileName);
            file.setFilePath(targetLocation.toString());
            file.setFileType(fileUpload.getContentType());
            fileUploadRepo.save(file);
            return file;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private static String generateUniqueFileName(String originalFilename) {
        String cleanedFilename = StringUtils.cleanPath(originalFilename);
        return cleanedFilename.replaceAll("[ ']", "_");
    }

    private void validateUpload(MultipartFile fileUpload, String fileName) {
        if (fileName.isBlank() || fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new RuntimeException("Invalid file name");
        }
        if (fileName.length() > 180) {
            throw new RuntimeException("File name is too long");
        }
        String contentType = fileUpload.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new RuntimeException("Unsupported file type");
        }
    }

    private Path resolveStoragePath(String fileName) {
        Path targetLocation = this.fileStorageLocation.resolve(fileName).normalize();
        if (!targetLocation.startsWith(this.fileStorageLocation)) {
            throw new RuntimeException("Invalid file path");
        }
        return targetLocation;
    }

    @Override
    @Transactional
    public void deleteFile(Integer id) {
        fileUploadRepo.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<FileUpload> editFile(FileUpload fileUpload) {
        if (fileUploadRepo.existsById(fileUpload.getId())) {
            return Optional.of(fileUploadRepo.save(fileUpload));
        }
        return Optional.empty();
    }

    @Override
    public List<FileUpload> getAllFiles() {
        return fileUploadRepo.findAll();
    }

    @Override
    public Optional<FileUpload> getFileById(Integer id) {
        return fileUploadRepo.findById(id);
    }

    @Override
    public List<FileUpload> getFilesByUserId(Integer userId) {
        return fileUploadRepo.findByUserId(userId);
    }

    @Override
    public List<UserFileDTO> getFileNamesByUserId(Integer userId) {
        return fileUploadRepo.findFileNamesByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteFileByUserIdAndFileName(Integer userId, String fileName) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
        fileUploadRepo.deleteByUserAndFileName(user, fileName);
    }
}

