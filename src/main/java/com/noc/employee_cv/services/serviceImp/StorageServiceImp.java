package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.ImageData;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.StorageRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StorageServiceImp {
    private final UserRepo userRepo;
    private final Path fileStorageLocation;

    @Autowired
    public StorageServiceImp(@Value("${file.upload-dir}") String uploadDir, UserRepo userRepo) {
        this.userRepo = userRepo;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    public User storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            User photo = new User();
            photo.setImageName(fileName);
            photo.setImagePath(targetLocation.toString());
            return photo;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    public User updateFile(MultipartFile file, Integer photoId) throws IOException {
        User photo = userRepo.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found with id " + photoId));

        // Delete the old file

        if(photo.getImageName()!=null) {
            Path targetLocation = this.fileStorageLocation.resolve(photo.getImageName());
            Files.deleteIfExists(targetLocation);
        }

        // Store the new file
        String fileName = StringUtils.cleanPath(photo.getUsername()+"__"+file.getOriginalFilename());
        Path newTargetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), newTargetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Update the Photo object
        photo.setImageName(fileName);
        photo.setImagePath(newTargetLocation.toString());
        return userRepo.save(photo);
    }

    public User getPhotoByUserId(Integer id){
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id " + id));
    }
}
