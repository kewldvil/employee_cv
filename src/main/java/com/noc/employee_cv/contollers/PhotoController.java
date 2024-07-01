package com.noc.employee_cv.contollers;

import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImpl.FileStorageServiceImpl;
import com.noc.employee_cv.services.serviceImpl.PhotoStorageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final UserRepo userRepo;
    private final PhotoStorageServiceImpl storageService;

    @Value("${file.photo-dir}")
    private String uploadDir;

    @Value("${file.photo-max-size}")
    private long MAX_PHOTO_SIZE;


    @PostMapping("/upload/")
    public ResponseEntity<Void> uploadPhoto(@RequestParam("photo") MultipartFile photo) {
        try {
            User storedPhoto = storageService.storeFile(photo);
            userRepo.save(storedPhoto);

            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(storedPhoto.getId().toString())
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> updatePhoto(@PathVariable Integer id,
                                              @RequestParam MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Check file size
        if (file.getSize() > MAX_PHOTO_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }
        try {
            User updatedPhoto = storageService.updateFile(file, id);
            userRepo.save(updatedPhoto);
            return ResponseEntity.ok(updatedPhoto.getImageName());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update photo");
        }
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<byte[]> getPhotoByUserId(@PathVariable Integer userId) {
        try {
            User user = storageService.getPhotoByUserId(userId);
            if (user != null && user.getImageName() != null) {
                Path filePath = Paths.get(uploadDir).resolve(user.getImageName());
                if (Files.exists(filePath)) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your file type
                    headers.setContentLength(fileContent.length);
                    headers.setContentDispositionFormData("attachment", user.getImageName());

                    return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/by-filename/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            if (filename != null && !filename.trim().isEmpty()) {
                // Decode the filename to handle Unicode characters properly
                String decodedFilename = new String(filename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                Path filePath = Paths.get(uploadDir).resolve(decodedFilename);
                if (Files.exists(filePath)) {
                    byte[] fileContent = Files.readAllBytes(filePath);

                    // Set headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your file type
                    headers.setContentLength(fileContent.length);
                    headers.setContentDispositionFormData("attachment", decodedFilename);

                    return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
