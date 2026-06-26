package com.noc.employee_cv.controller;

import com.noc.employee_cv.dto.GeneralDepartmentDTO;
import com.noc.employee_cv.model.GeneralDepartment;
import com.noc.employee_cv.services.serviceImpl.DepartmentServiceImpl;
import com.noc.employee_cv.services.serviceImpl.GeneralDepartmentServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.noc.employee_cv.controller.EmployeeController.convertToDTO;

@RestController
@RequestMapping("/api/v1/general-department")
@RequiredArgsConstructor
public class GeneralDepartmentController {
    private final GeneralDepartmentServiceImpl generalDepartmentServiceImpl;
    private final DepartmentServiceImpl departmentServiceImpl;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GeneralDepartmentDTO> createGeneralDepartment(@RequestBody GeneralDepartmentDTO generalDepartmentDTO) {
        GeneralDepartment generalDepartment = new GeneralDepartment();
        generalDepartment.setName(generalDepartmentDTO.getName());
        generalDepartment.setEnabled(generalDepartmentDTO.isEnabled());
        generalDepartmentServiceImpl.save(generalDepartment);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Iterable<GeneralDepartment>> getAllGeneralDepartments() {
        return ResponseEntity.ok(generalDepartmentServiceImpl.findAll());
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GeneralDepartment> updateGeneralDepartment(
            @RequestBody GeneralDepartmentDTO generalDepartmentDTO) {
        // Convert DTO to entity
        GeneralDepartment generalDepartment = new GeneralDepartment();
        generalDepartment.setId(generalDepartmentDTO.getId());
        generalDepartment.setName(generalDepartmentDTO.getName());
        generalDepartment.setEnabled(generalDepartmentDTO.isEnabled());

        // Check if the general department is being disabled
        if (!generalDepartment.isEnabled()) {
            departmentServiceImpl.disableDepartmentsByGeneralDepartmentId(generalDepartment.getId());
        }

        // Save the updated general department
        GeneralDepartment updatedDepartment = generalDepartmentServiceImpl.save(generalDepartment);

        // Return the updated entity in the response
        return ResponseEntity.accepted().body(updatedDepartment);
    }


}
