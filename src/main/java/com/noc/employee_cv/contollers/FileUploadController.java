package com.noc.employee_cv.contollers;


import com.noc.employee_cv.dto.FileResponseDTO;
import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.File;
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
public class FileUploadController {
    private static final int MAX_FILE_COUNT = 10; // Limit to 5 files
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserRepo userRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/user-files/{userId}")
    public ResponseEntity<List<FileResponseDTO>> getUserFiles(@PathVariable Integer userId) {
        List<FileUpload> files = fileUploadService.getFilesByUserId(userId);
        List<FileResponseDTO> uploadedFiles = new ArrayList<>();
        if (files.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            for (FileUpload file : files) {
                if (file.getFileName() != null) {
                    try {
                        Path path = Paths.get(this.uploadDir + file.getFileName());
                        String fileType = Files.probeContentType(path);
                        byte[] fileContent = Files.readAllBytes(path);
                        String base64Content = Base64.getEncoder().encodeToString(fileContent);

                        FileResponseDTO fileResponse = new FileResponseDTO();
                        fileResponse.setFileName(file.getFileName());
                        fileResponse.setFileType(fileType);
                        fileResponse.setBase64Content(base64Content);
                        fileResponse.setFileUrl(file.getFilePath());

                        uploadedFiles.add(fileResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
            return new ResponseEntity<>(uploadedFiles, HttpStatus.OK);
        }
    }


    //    @GetMapping("/file/{fileId}")
//    public ResponseEntity<byte[]> getPhotoByUserId(@PathVariable Integer fileId) throws IOException {
//        FileUpload file = fileUploadService.getFileById(fileId).orElseThrow();
//        if(file.getFileName()!=null) {
//            byte[] fileContent = Files.readAllBytes(Paths.get(this.uploadDir + user.getImageName()));
//            return ResponseEntity.ok(fileContent);
//        }else return null;
//    }
    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] files,
                                         @PathVariable("userId") Integer userId) {
        // Check file count limit
        if (files.length > MAX_FILE_COUNT) {
            return new ResponseEntity<>("Maximum file upload limit is " + MAX_FILE_COUNT, HttpStatus.BAD_REQUEST);
        }

        // Find user by ID
        User user = userRepo.findUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // List to store URIs of uploaded files
        List<URI> fileUris = new ArrayList<>();

        // Process each file
        for (MultipartFile file : files) {
            try {
                FileUpload fileUpload = fileUploadService.uploadFile(file, user);

                URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(fileUpload.getId().toString())
                        .build()
                        .toUri();
                fileUris.add(uri);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to upload file: " + file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return ResponseEntity.created(fileUris.isEmpty() ? null : fileUris.get(0)).body(fileUris);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer id) {
        fileUploadService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @PutMapping("/edit/{id}")
//    public ResponseEntity<FileUpload> editFile(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
//        try {
//            Optional<FileUpload> existingFile = fileUploadService.getFileById(id);
//            if (existingFile.isPresent()) {
//                FileUpload fileUpload = existingFile.get();
//                fileUpload.setFileName(file.getOriginalFilename());
//                fileUpload.setFileType(file.getContentType());
//                fileUpload.setData(file.getBytes());
//                Optional<FileUpload> updatedFile = fileUploadService.editFile(fileUpload);
//                return updatedFile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<List<FileUpload>> getAllFiles() {
        List<FileUpload> files = fileUploadService.getAllFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileUpload> getFileById(@PathVariable Integer id) {
        Optional<FileUpload> fileUpload = fileUploadService.getFileById(id);
        return fileUpload.map(file -> new ResponseEntity<>(file, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
