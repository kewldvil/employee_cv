package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.EmployeeAddress;

import java.util.List;

public interface EmployeeAddressService {
    void save(EmployeeAddress employeeAddress);
    EmployeeAddress findById(Integer id);
    List<EmployeeAddress> findAll();
    void deleteById(Integer id);
    void update(EmployeeAddress employeeAddress);
}
