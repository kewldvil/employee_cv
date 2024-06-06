package com.noc.employee_cv.contollers;


import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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

    //    @PostMapping("/upload/{userId}")
//    public ResponseEntity<FileUpload> uploadFile(@RequestParam("file") MultipartFile file,
//                                                 @PathVariable("userId") Integer userId) throws IOException{
//        System.out.println(file);
//        try {
//            User user = userRepo.findUserById(userId);
//
//            FileUpload fileUpload = new FileUpload();
//            fileUpload.setFileName(file.getOriginalFilename());
//            fileUpload.setFileType(file.getContentType());
//            fileUpload.setData(file.getBytes());
//            fileUpload.setUser(user);
//            // You need to set the employee object here based on employeeId
//            // For simplicity, assuming an Employee object can be fetched and set
//            // fileUpload.setEmployee(employee);
//            FileUpload savedFile = fileUploadService.uploadFile(fileUpload);
//            return new ResponseEntity<>(savedFile, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping("/user-files/{userId}")
    public ResponseEntity<List<FileUpload>> getUserFiles(@PathVariable Integer userId) {
        List<FileUpload> files = fileUploadService.getFilesByUserId(userId);
        if (files.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            System.out.println(files.size());
            return new ResponseEntity<>(files, HttpStatus.OK);
        }
    }
    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] files,
                                         @PathVariable("userId") Integer userId) {
        if (files.length > MAX_FILE_COUNT) {
            return new ResponseEntity<>("Maximum file upload limit is " + MAX_FILE_COUNT, HttpStatus.BAD_REQUEST);
        }

        try {
            User user = userRepo.findUserById(userId);

            for (MultipartFile file : files) {
                FileUpload fileUpload = new FileUpload();
                fileUpload.setFileName(file.getOriginalFilename());
                fileUpload.setFileType(file.getContentType());
                fileUpload.setData(file.getBytes());
                fileUpload.setUser(user);
                fileUploadService.uploadFile(fileUpload);
            }
            return new ResponseEntity<>("Files uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer id) {
        fileUploadService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<FileUpload> editFile(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            Optional<FileUpload> existingFile = fileUploadService.getFileById(id);
            if (existingFile.isPresent()) {
                FileUpload fileUpload = existingFile.get();
                fileUpload.setFileName(file.getOriginalFilename());
                fileUpload.setFileType(file.getContentType());
                fileUpload.setData(file.getBytes());
                Optional<FileUpload> updatedFile = fileUploadService.editFile(fileUpload);
                return updatedFile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
