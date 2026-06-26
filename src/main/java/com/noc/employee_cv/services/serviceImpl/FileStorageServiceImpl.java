package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.UserRepo;
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
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FileStorageServiceImpl {
    private final UserRepo userRepo;
    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir, UserRepo userRepo) {
        this.userRepo = userRepo;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public User storeFile(MultipartFile file) {
        String fileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Path targetLocation = resolveFilePath(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            User photo = new User();
            photo.setImageName(fileName);
            photo.setImagePath(targetLocation.toString());
            return photo;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Transactional
    public User updateFile(MultipartFile file, Integer photoId) throws IOException {
        User photo = userRepo.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found with id " + photoId));

        if (photo.getImageName() != null) {
            Path targetLocation = resolveFilePath(photo.getImageName());
            Files.deleteIfExists(targetLocation);
        }

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        Path newTargetLocation = resolveFilePath(fileName);
        Files.copy(file.getInputStream(), newTargetLocation, StandardCopyOption.REPLACE_EXISTING);

        photo.setImageName(fileName);
        photo.setImagePath(newTargetLocation.toString());
        return userRepo.save(photo);
    }

    public User getPhotoByUserId(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id " + id));
    }

    private String generateUniqueFileName(String originalFilename) {
        String uniqueID = UUID.randomUUID().toString();
        String cleanedFilename = StringUtils.cleanPath(originalFilename);
        String filenameWithoutSpaces = StringUtils.replace(cleanedFilename, " ", "_");
        return uniqueID + "_" + filenameWithoutSpaces;
    }

    private Path resolveFilePath(String fileName) {
        String cleanedFilename = StringUtils.cleanPath(fileName);
        if (cleanedFilename.isBlank() || cleanedFilename.contains("..")) {
            throw new RuntimeException("Invalid file name");
        }
        Path path = this.fileStorageLocation.resolve(cleanedFilename).normalize();
        if (!path.startsWith(this.fileStorageLocation)) {
            throw new RuntimeException("Invalid file path");
        }
        return path;
    }
}
