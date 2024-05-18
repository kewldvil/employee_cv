package com.noc.employee_cv.contollers;

import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImp.StorageServiceImp;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final UserRepo userRepo;
    private final StorageServiceImp storageService;
    @Value("${file.upload-dir}")
    private String uploadDir;
    @PostConstruct
    public void init() throws IOException {
        Path fileStorageLocation = Path.of(uploadDir);
    }

    @PostMapping("/upload/")
    public ResponseEntity<Void> uploadPhoto(@RequestParam("photo") MultipartFile photo) {
        User storedPhoto = storageService.storeFile(photo);
        userRepo.save(storedPhoto);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(storedPhoto.getId().toString())
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> updatePhoto(@PathVariable Integer id,
                                              @RequestParam MultipartFile file) throws IOException {
        User updatedPhoto = storageService.updateFile(file, id);
        userRepo.save(updatedPhoto);

        return ResponseEntity.ok(updatedPhoto.getImageName());
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<byte[]> getPhotoByUserId(@PathVariable Integer userId) throws IOException {
        User user = storageService.getPhotoByUserId(userId);
        if(user.getImageName()!=null) {
            byte[] fileContent = Files.readAllBytes(Paths.get(this.uploadDir + user.getImageName()));
            return ResponseEntity.ok(fileContent);
        }else return null;
    }

    @GetMapping(value = "/by-filename/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {

        byte[] fileContent = Files.readAllBytes(Paths.get(this.uploadDir+filename));
        return ResponseEntity.ok(fileContent);
    }
}
