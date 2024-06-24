package com.noc.employee_cv.contollers;


import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.PoliceRank;
import com.noc.employee_cv.enums.Position;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.AppreciationRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImp.EmployeeServiceImp;
import com.noc.employee_cv.services.serviceImp.ReportServiceImp;
import com.noc.employee_cv.services.serviceImp.StorageServiceImp;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImp service;
    private final UserRepo userRepo;
    private final ReportServiceImp reportService;
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> createNewEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        service.save(req);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        System.out.println("update employee");
        service.update(req);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) throws MessagingException {
        System.out.println("GET EMPLOYEE BY ID");
        Employee employee = service.findById(id);
        if (employee != null) {
            // If response body is not null, return it with HTTP status 200 OK
            return ResponseEntity.ok(employee);
        } else {
            // If response body is null, return 404 Not Found status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeByUserId(@PathVariable Integer id) throws MessagingException {
        System.out.println("GET EMPLOYEE BY USER ID");
        Employee employee = service.findByUserId(id);
        if (employee != null) {
            // If response body is not null, return it with HTTP status 200 OK
            return ResponseEntity.ok(employee);
        } else {
            // If response body is null, return 404 Not Found status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{userId}/{employeeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeByUserIdAndEmployeeId(@PathVariable Integer userId, @PathVariable Integer employeeId) throws MessagingException {
        System.out.println("getEmployeeByUserIdAndEmployeeId");
        Employee employee = service.findByUserIdAndEmployeeId(employeeId, userId);

        if (employee != null) {
            // If response body is not null, return it with HTTP status 200 OK
            return ResponseEntity.ok(employee);
        } else {
            // If response body is null, return 404 Not Found status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("GET ALL USERS");
        List<User> users = userRepo.findAll();
//        System.out.println(users);
        // Check if users list is not null or empty
        if (!users.isEmpty()) {
            // If users list is not empty, return it with HTTP status 200 OK
            return ResponseEntity.ok(users);
        } else {
            // If users list is empty, return 404 Not Found status code
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/report/{format}/{empId}")
    public String generateReport(@PathVariable String format,@PathVariable Integer empId) throws JRException, IOException {
        return reportService.exportReport(format,empId);
    }

}