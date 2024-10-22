package com.noc.employee_cv.contollers;

import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImpl.FileStorageServiceImpl;
import com.noc.employee_cv.services.serviceImpl.PhotoStorageServiceImpl;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
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
                    // Read original image bytes
                    byte[] originalImageBytes = Files.readAllBytes(filePath);

                    // Generate thumbnail using Thumbnalator
                    ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
                    Thumbnails.of(filePath.toFile())
                            .size(150, 150) // Adjust size for thumbnail
                            .toOutputStream(thumbnailOutputStream);

                    byte[] thumbnailBytes = thumbnailOutputStream.toByteArray();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your file type
                    headers.setContentLength(thumbnailBytes.length);
                    headers.setContentDispositionFormData("attachment", "thumbnail_" + user.getImageName());

                    return new ResponseEntity<>(thumbnailBytes, headers, HttpStatus.OK);
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
    @GetMapping("/photo-full-size/{userId}")
    public ResponseEntity<byte[]> getActualSizePhotoByUserId(@PathVariable Integer userId) {
        try {
            User user = storageService.getPhotoByUserId(userId);
            if (user != null && user.getImageName() != null) {
                Path filePath = Paths.get(uploadDir).resolve(user.getImageName());
                if (Files.exists(filePath)) {
                    // Read original image file
                    ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();

                    // Compress the image by reducing its quality to 50%
                    Thumbnails.of(filePath.toFile())
                            .scale(1) // Keep original dimensions
                            .outputQuality(0.6) // 50% quality
                            .toOutputStream(compressedOutputStream);

                    byte[] compressedImageBytes = compressedOutputStream.toByteArray();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG); // Adjust this based on your image type
                    headers.setContentLength(compressedImageBytes.length);
                    headers.setContentDispositionFormData("attachment", "compressed_" + user.getImageName());

                    return new ResponseEntity<>(compressedImageBytes, headers, HttpStatus.OK);
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

//    @GetMapping(value = "/by-filename/{filename}")
//    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
//        try {
//            if (filename != null && !filename.trim().isEmpty()) {
//                // Decode the filename to handle Unicode characters properly
//                String decodedFilename = new String(filename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//
//                Path filePath = Paths.get(uploadDir).resolve(decodedFilename);
//                if (Files.exists(filePath)) {
//                    byte[] fileContent = Files.readAllBytes(filePath);
//
//                    // Set headers
//                    HttpHeaders headers = new HttpHeaders();
//                    headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your file type
//                    headers.setContentLength(fileContent.length);
//                    headers.setContentDispositionFormData("attachment", decodedFilename);
//
//                    return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//                } else {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//            }
//        } catch (IOException ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping(value = "/by-filename/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            if (filename != null && !filename.trim().isEmpty()) {
                // Decode the filename to handle Unicode characters properly
                String decodedFilename = new String(filename.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                Path filePath = Paths.get(uploadDir).resolve(decodedFilename);
                if (Files.exists(filePath)) {
                    // Detect MIME type dynamically
                    String mimeType = Files.probeContentType(filePath);
                    if (mimeType == null || !mimeType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
                    }

                    // Extract the image format (e.g., png, jpeg) from the MIME type
                    String imageFormat = mimeType.substring(mimeType.lastIndexOf('/') + 1);

                    // Compress and resize the image
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    Thumbnails.of(filePath.toFile())
//                            .size(150, 150) // Adjust the size as needed
//                            .outputFormat(imageFormat) // Output in the same format
//                            .outputQuality(1) // Adjust the compression quality (0.0 to 1.0)
//                            .toOutputStream(outputStream);
                    // Generate thumbnail using Thumbnalator
                    ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
                    Thumbnails.of(filePath.toFile())
                            .size(150, 150) // Adjust size for thumbnail
                            .toOutputStream(thumbnailOutputStream);
                    byte[] compressedImage = thumbnailOutputStream.toByteArray();

                    // Set headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(mimeType)); // Set dynamically based on MIME type
                    headers.setContentLength(compressedImage.length);
                    headers.setContentDispositionFormData("attachment", decodedFilename);

                    return new ResponseEntity<>(compressedImage, headers, HttpStatus.OK);
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
