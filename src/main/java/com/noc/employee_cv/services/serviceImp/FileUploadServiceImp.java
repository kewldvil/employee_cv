package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.FileUploadRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Over;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileUploadServiceImp implements FileUploadService {
    private final FileUploadRepo fileUploadRepo;
    private final Path fileStorageLocation;
    private final UserRepo userRepo;

    @Autowired
    public FileUploadServiceImp(@Value("${file.upload-dir}") String uploadDir, FileUploadRepo fileUploadRepo, UserRepo userRepo) {
        this.fileUploadRepo = fileUploadRepo;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public FileUpload uploadFile(MultipartFile fileUpload, User user) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Check for existing file by this user with the same name
            FileUpload existingFile = fileUploadRepo.findByUserAndFileName(user, fileName);
            if (existingFile != null) {
                // Delete the existing file from the storage
                Path existingFilePath = Paths.get(existingFile.getFilePath());
                Files.deleteIfExists(existingFilePath);

                // Delete the existing file record from the repository
                fileUploadRepo.delete(existingFile);
            }

            //store the new file
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
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

    @Override
    public void deleteFile(Integer id) {
        fileUploadRepo.deleteById(id);
    }

    @Override
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

    @Transactional
    @Override
    public List<FileUpload> getFilesByUserId(Integer userId) {
        return fileUploadRepo.findByUserId(userId);
    }

    @Transactional
    @Override
    public void deleteFileByUserIdAndFileName(Integer userId, String fileName) {
        User user = userRepo.findById(userId).orElseThrow();
        fileUploadRepo.deleteByUserAndFileName(user, fileName);
    }
}
