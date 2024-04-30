package com.noc.employee_cv.contollers;

import com.noc.employee_cv.authentication.RegistrationRequest;
import com.noc.employee_cv.dto.AppreciationDTO;
import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.services.serviceImp.EmployeeImp;
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
    private final EmployeeImp service;

    @Transactional
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> saveEmployee(
            @RequestBody @Valid EmployeeDTO req
    ) throws MessagingException {
        System.out.println(req.toString());
//        Employee res = new Employee();
//        Set<Address> adrList= new HashSet<Address>();
//        Address empPOB= new Address();
//        empPOB.setAddressType(req.getAddressType());
//        Address empCurAdr= new Address();
//        res.setFirstname(req.getFirstname());
//        res.setLastname(req.getLastname());
//        res.setGender(req.getGender());
//        res.setDateOfBirth(req.getDateOfBirth());
//        res.setEmployeeAddress(req.getCurrentAddress().getAddress());

//        System.out.println(req.toString());
//        service.save(employee);
        return ResponseEntity.accepted().build();
    }
}

