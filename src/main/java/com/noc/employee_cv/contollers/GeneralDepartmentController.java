package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.GeneralDepartmentDTO;
import com.noc.employee_cv.models.GeneralDepartment;
import com.noc.employee_cv.services.serviceImpl.GeneralDepartmentServiceImp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.noc.employee_cv.contollers.EmployeeController.convertToDTO;

@RestController
@RequestMapping("/api/v1/general-department")
@RequiredArgsConstructor
public class GeneralDepartmentController {
    private final GeneralDepartmentServiceImp generalDepartmentServiceImp;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GeneralDepartmentDTO> createGeneralDepartment(@RequestBody GeneralDepartmentDTO generalDepartmentDTO) {
        GeneralDepartment generalDepartment = new GeneralDepartment();
        generalDepartment.setName(generalDepartmentDTO.getName());
        generalDepartment.setEnabled(generalDepartmentDTO.isEnabled());
        generalDepartmentServiceImp.save(generalDepartment);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Iterable<GeneralDepartment>> getAllGeneralDepartments() {
        return ResponseEntity.ok(generalDepartmentServiceImp.findAll());
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<GeneralDepartment> updateGeneralDepartment(
            @RequestBody GeneralDepartmentDTO generalDepartmentDTO) {
        GeneralDepartment generalDepartment = new GeneralDepartment();
        generalDepartment.setId(generalDepartmentDTO.getId());
        generalDepartment.setName(generalDepartmentDTO.getName());
        generalDepartment.setEnabled(generalDepartmentDTO.isEnabled());
        generalDepartmentServiceImp.update(generalDepartment);
        return ResponseEntity.accepted().build();
    }

}
