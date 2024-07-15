package com.noc.employee_cv.contollers;


import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.UserEmployeeDTO;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImpl.EmployeeServiceImp;
import com.noc.employee_cv.services.serviceImpl.ReportServiceImp;
import jakarta.mail.MessagingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


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
    public ResponseEntity<List<UserEmployeeDTO>> getAllUsers() {
        System.out.println("GET ALL USERS");
        List<User> users = userRepo.findAll();
        System.out.println(users.get(95));
//        System.out.println(users);
        // Check if users list is not null or empty
        if (!users.isEmpty()) {
            List<UserEmployeeDTO> employees = users.stream().map(EmployeeController::convertToDTO).toList();
            // If users list is not empty, return it with HTTP status 200 OK
            return ResponseEntity.ok(employees);
        } else {
            // If users list is empty, return 404 Not Found status code
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/report/{format}/{empId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format, @PathVariable Integer empId) throws JRException, IOException {
        return reportService.exportReportToFrontEnd(format, empId);
    }

    public static UserEmployeeDTO convertToDTO(User user) {
        UserEmployeeDTO userEmployeeDTO = new UserEmployeeDTO();
        userEmployeeDTO.setId(user.getId());
        userEmployeeDTO.setImageName(user.getImageName());
        userEmployeeDTO.setImagePath(user.getImagePath());
        userEmployeeDTO.setFirstname(user.getFirstname());
        userEmployeeDTO.setLastname(user.getLastname());
        userEmployeeDTO.getFullName();
        if (user.getEmployee() != null) {
            userEmployeeDTO.setGender(user.getEmployee().getGender());
            userEmployeeDTO.setIsMarried(user.getEmployee().getIsMarried());

            userEmployeeDTO.setCurrentPosition(user.getEmployee().getCurrentPosition());
            userEmployeeDTO.setCurrentPoliceRank(user.getEmployee().getCurrentPoliceRank());
            userEmployeeDTO.setDegreeLevels(user.getEmployee().getEmployeeDegreeLevels());
        }

        return userEmployeeDTO;
    }
}