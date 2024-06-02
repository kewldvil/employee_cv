package com.noc.employee_cv.contollers;

import com.noc.employee_cv.services.serviceImp.EmployeeServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/managements")
@RequiredArgsConstructor
public class DashboardController {
    private final EmployeeServiceImp employeeService;
    @GetMapping("/total-employee")
    public ResponseEntity<Long> getTotalEmployees() {
        long totalEmployees = employeeService.getTotalEmployees();
        return ResponseEntity.ok(totalEmployees);
    }
}
