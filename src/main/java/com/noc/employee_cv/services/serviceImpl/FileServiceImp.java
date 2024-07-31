package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.FileUploadRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.FileService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileServiceImp implements FileService {

    private final FileUploadRepo fileUploadRepo;
    private final Path fileStorageLocation;
    private final UserRepo userRepo;

    @Autowired
    public FileServiceImp(@Value("${file.upload-dir}") String uploadDir,
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

    //    private String generateUniqueFileName(String originalFilename) {
//        String uniqueID = UUID.randomUUID().toString();
//        String cleanedFilename = StringUtils.cleanPath(originalFilename);
//        String filenameWithoutSpaces = StringUtils.replace(cleanedFilename, " ", "_");
//        return uniqueID + "_" + filenameWithoutSpaces;
//    }
    private static String generateUniqueFileName(String originalFilename) {
        String cleanedFilename = StringUtils.cleanPath(originalFilename);
        // Replace single quotes and spaces with underscores using regex
        return cleanedFilename.replaceAll("[ ']", "_");
    }


    @Override
    @Transactional
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

    @Override
    @Transactional
    public List<FileUpload> getFilesByUserId(Integer userId) {
        return fileUploadRepo.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteFileByUserIdAndFileName(Integer userId, String fileName) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
        fileUploadRepo.deleteByUserAndFileName(user, fileName);
    }
}


