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
        Employee res = new Employee();
        res.setFirstname(req.getFirstname());
        res.setLastname(req.getLastname());
        res.setGender(req.getGender());
        res.setDateOfBirth(req.getDateOfBirth());

        Set<Address> adrList= new HashSet<Address>();
        Address empPOB= new Address();
        Address empCurrentAdr= new Address();
        Address spouseCurrentAdr= new Address();
        Address spousePOB= new Address();
        Address fatherPOB= new Address();
        Address fatherCurrentAdr= new Address();
        Address motherPOB= new Address();
        Address motherCurrentAdr= new Address();

//        empPOB=(Address)req.getPlaceOfBirth();




        System.out.println(req.toString());
//        service.save(employee);
        return ResponseEntity.accepted().build();
    }
}

