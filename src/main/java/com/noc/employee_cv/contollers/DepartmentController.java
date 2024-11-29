package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.DepartmentDTO;
import com.noc.employee_cv.dto.DepartmentDTO;
import com.noc.employee_cv.models.Department;
import com.noc.employee_cv.models.GeneralDepartment;
import com.noc.employee_cv.repositories.GeneralDepartmentRepo;
import com.noc.employee_cv.services.serviceImpl.DepartmentServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentServiceImp departmentService;
    private final GeneralDepartmentRepo generalDepartmentRepo;
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Department> createGeneralDepartment(@RequestBody DepartmentDTO departmentDTO) {

        GeneralDepartment generalDepartment = generalDepartmentRepo.findById(departmentDTO.getGeneralDepartmentId()).orElseThrow();
        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setEnabled(departmentDTO.isEnabled());
        department.setGeneralDepartment(generalDepartment);
        departmentService.save(department);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getAllDepartments() {
        System.out.println("GET ALL DEPARTMENTS");

        // Fetch all enabled general departments
        List<GeneralDepartment> generalDepartments = generalDepartmentRepo.findAllByEnabledTrue();

        // Fetch all departments
        List<Department> departments = departmentService.findAll();

        // Create a map to hold both lists
        Map<String, Object> result = new HashMap<>();
        result.put("generalDepartments", generalDepartments);
        result.put("departments", departments);

        // Return the map wrapped in a ResponseEntity
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Department>> getByGeneralDepartmentId(@PathVariable("id") Integer generalDepartmentId) {
        System.out.println("Fetching departments by General Department ID: " + generalDepartmentId);

        // Fetch departments by general department ID
        List<Department> departments = departmentService.findAllByGeneralDepartmentId(generalDepartmentId);

        // Check if the list is empty and respond accordingly
        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content
        }

        return ResponseEntity.ok(departments); // Returns 200 OK with the list of departments
    }



    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Department> updateDepartment(
            @RequestBody DepartmentDTO departmentDTO) {

        // Find existing Department and GeneralDepartment
        Department department = departmentService.findById(departmentDTO.getId());
        GeneralDepartment generalDepartment = generalDepartmentRepo.findById(departmentDTO.getGeneralDepartmentId()).orElseThrow();
        // Update Department fields
        department.setName(departmentDTO.getName());
        department.setEnabled(departmentDTO.isEnabled());
        department.setGeneralDepartment(generalDepartment);

        // Save updated Department
        departmentService.update(department);

        return ResponseEntity.accepted().body(department);
    }
    @PutMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Department> disableDepartment(
             @PathVariable Integer departmentId) {
        // Find existing Department and GeneralDepartment
        Department department = departmentService.findById(departmentId);
        // Update Department fields
        department.setEnabled(false);
        // Save updated Department
        departmentService.update(department);

        return ResponseEntity.accepted().body(department);
    }
}

