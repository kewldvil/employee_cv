package com.noc.employee_cv.contollers;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.services.serviceImp.EmployeeImp;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeImp service;
    @PostMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> register(
            @RequestBody @Valid Employee employee
            ) throws MessagingException {
        service.save(employee);
        return ResponseEntity.accepted().build();
    }
}
