package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.DepartmentDTO;
import com.noc.employee_cv.dto.DepartmentDTO;
import com.noc.employee_cv.models.Department;

import com.noc.employee_cv.models.GeneralDepartment;
import com.noc.employee_cv.repositories.GeneralDepartmentRepo;
import com.noc.employee_cv.services.serviceImpl.DepartmentServiceImp;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentServiceImp departmentService;
    private final GeneralDepartmentRepo generalDepartmentRepo;
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> listAllDepartments() {

        List<Department> departments = departmentService.findAll();

        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }

        // Map Department to DepartmentDTO using stream
        List<DepartmentDTO> departmentDTOs = departments.stream()
                .map(department -> new DepartmentDTO(
                        department.getId(),
                        department.getName(),
                        department.isEnabled(),
                        department.getGeneralDepartment().getName(),
                        department.getGeneralDepartment().getId() // Assuming GeneralDepartment has getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(departmentDTOs); // Return 200 OK with DTO list
    }

    @GetMapping("/by-general-department")
    public ResponseEntity<List<DepartmentDTO>> departmentByGeneralDepartmentId(@RequestParam(required = false, defaultValue = "0") Integer generalDepartmentId) {

        if (generalDepartmentId == 0) {
            return ResponseEntity.ok(Collections.emptyList()); // Return empty list if generalDepartmentId is default value (0)
        }

        List<Department> departments = departmentService.findAllByGeneralDepartmentId(generalDepartmentId);

        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }

        // Map Department to DepartmentDTO using stream
        List<DepartmentDTO> departmentDTOs = departments.stream()
                .map(department -> new DepartmentDTO(
                        department.getId(),
                        department.getName(),
                        department.isEnabled(),
                        department.getGeneralDepartment().getName(),
                        department.getGeneralDepartment().getId() // Assuming GeneralDepartment has getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(departmentDTOs); // Return 200 OK with DTO list
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentDTO department) {
        GeneralDepartment generalDepartment = generalDepartmentRepo.findById(department.getGeneralDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("GeneralDepartment not found with ID: " + department.getGeneralDepartmentId()));
        Department d = new Department();
        d.setEnabled(true);
        d.setName(department.getName());
        d.setGeneralDepartment(generalDepartment);
        departmentService.save(d);
        return ResponseEntity.accepted().build();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Department> updateDepartment(@RequestBody DepartmentDTO department) {
        System.out.println(department);

        // Fetch the existing Department from the database
        Department existingDepartment = departmentService.findById(department.getId());


        // Update fields
        GeneralDepartment generalDepartment = generalDepartmentRepo.findById(department.getGeneralDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("GeneralDepartment not found with ID: " + department.getGeneralDepartmentId()));

        existingDepartment.setName(department.getName());
        existingDepartment.setEnabled(department.isEnabled());
        existingDepartment.setGeneralDepartment(generalDepartment);

        // Save the updated entity
        Department updatedDepartment = departmentService.save(existingDepartment);

        // Return 200 OK with the updated entity
        return ResponseEntity.ok(updatedDepartment);
    }


}

