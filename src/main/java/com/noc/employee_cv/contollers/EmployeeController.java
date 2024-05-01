package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.BloodType;
import com.noc.employee_cv.enums.EducationLevel;
import com.noc.employee_cv.enums.PoliceRank;
import com.noc.employee_cv.enums.Position;
import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.services.serviceImp.EmployeeServiceImp;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImp service;

    @Transactional
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> saveEmployee(
            @RequestBody @Valid EmployeeDTO req
    ) throws MessagingException {
        System.out.println(req.toString());
        Employee res = new Employee();

        res.setBloodType(BloodType.valueOf(req.getBloodType()));
        res.setCurrentPoliceRank(PoliceRank.valueOf(req.getCurrentPoliceRank()));
        res.setCurrentPosition(Position.valueOf(req.getCurrentPosition()));
        res.setDateJoinGovernmentJob(req.getDateJoinGov());
        res.setDateJoinPoliceJob(req.getDateJoinPolice());
        res.setDateOfBirth(req.getDateOfBirth());
//        res.setEducationLevel(EducationLevel.valueOf(req.getEducationList()));









        System.out.println(req.toString());
//        service.save(employee);
        return ResponseEntity.accepted().build();
    }
}

