package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.FileResponseDTO;
import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.FileService;
import com.noc.employee_cv.services.serviceImpl.FileServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private static final int MAX_FILE_COUNT = 10; // Limit to 10 files

    private final FileServiceImp fileService;

    private final UserRepo userRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Value("${file.file-max-size}")
    private long MAX_FILE_SIZE;

    @GetMapping("/user-files/{userId}")
    public ResponseEntity<List<FileResponseDTO>> getUserFiles(@PathVariable Integer userId) {
        System.out.println("LOADING FILE");
        List<FileUpload> files = fileService.getFilesByUserId(userId);
        if (files.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<FileResponseDTO> uploadedFiles = new ArrayList<>();
        for (FileUpload file : files) {

            try {
                Path path = Paths.get(this.uploadDir + file.getFileName());
                String fileType = Files.probeContentType(path);
                byte[] fileContent = Files.readAllBytes(path);
                String base64Content = Base64.getEncoder().encodeToString(fileContent);

                FileResponseDTO fileResponse = new FileResponseDTO();
                fileResponse.setId(file.getId());
                fileResponse.setName(file.getFileName());
                fileResponse.setType(fileType);
                fileResponse.setBase64Content(base64Content);
                fileResponse.setUrl(file.getFilePath());

                uploadedFiles.add(fileResponse);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok(uploadedFiles);
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] files,
                                         @PathVariable("userId") Integer userId) {
        // Check file count limit
        if (files.length > MAX_FILE_COUNT) {
            return ResponseEntity.badRequest().body("Maximum file upload limit is " + MAX_FILE_COUNT);
        }

        // Find user by ID
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // List to store URIs of uploaded files
        List<URI> fileUris = new ArrayList<>();

        // Process each file
        for (MultipartFile file : files) {
            // Check if the file is empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Check file size
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
            }
            try {
                FileUpload fileUpload = fileService.uploadFile(file, user);

                URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/files/")
                        .path(fileUpload.getId().toString())
                        .build()
                        .toUri();
                fileUris.add(uri);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + file.getOriginalFilename());
            }
        }

        return ResponseEntity.created(fileUris.isEmpty() ? null : fileUris.get(0)).body(fileUris);
    }

    @DeleteMapping("/delete/{userId}/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable Integer userId, @PathVariable String fileName) {
        fileService.deleteFileByUserIdAndFileName(userId, fileName);
        try {
            Path path = Paths.get(uploadDir + fileName);
            Files.deleteIfExists(path);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete file " + fileName + ". Please try again!");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileUpload>> getAllFiles() {
        List<FileUpload> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileUpload> getFileById(@PathVariable Integer id) {
        Optional<FileUpload> fileUpload = fileService.getFileById(id);
        return fileUpload.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user-filenames/{userId}")
    public List<String> getUserFileNames(@PathVariable Integer userId) {
        return fileService.getFileNamesByUserId(userId);
    }
}
