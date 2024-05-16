package com.noc.employee_cv.contollers;


import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.PoliceRank;
import com.noc.employee_cv.enums.Position;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.AppreciationRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImp.EmployeeServiceImp;
import com.noc.employee_cv.services.serviceImp.StorageServiceImp;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImp service;
    private final AppreciationRepo appreciationRepo;
    private final StorageServiceImp storageService;
    private final UserRepo userRepo;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> createNewEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        service.save(req);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) throws MessagingException {
        System.out.println("getEmployeeById");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Employee employee = service.findById(id);
        if(employee == null) return null;else return ResponseEntity.ok(employee);
    }

    @PostConstruct
    public void init() throws IOException {
        Path fileStorageLocation = Path.of(uploadDir);
    }

    @PostMapping("/image/")
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

    @PutMapping("/updatePhoto/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> updatePhoto(@PathVariable Integer id,
                                            @RequestParam MultipartFile file) throws IOException {
        User updatedPhoto = storageService.updateFile(file, id);
        userRepo.save(updatedPhoto);

        return ResponseEntity.ok(updatedPhoto.getImageName());
    }

    @GetMapping("/user/photo/{userId}")
    public ResponseEntity<byte[]> getPhotoByUserId(@PathVariable Integer userId) throws IOException {
        User user = storageService.getPhotoByUserId(userId);
        if(user.getImageName()!=null) {
            byte[] fileContent = Files.readAllBytes(Paths.get(this.uploadDir + user.getImageName()));
            return ResponseEntity.ok(fileContent);
        }else return null;
    }

    @GetMapping(value = "/photos/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {

            byte[] fileContent = Files.readAllBytes(Paths.get(this.uploadDir+filename));
        return ResponseEntity.ok(fileContent);
    }

}

