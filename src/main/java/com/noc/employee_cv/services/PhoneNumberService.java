package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.PhoneNumber;

import java.util.List;

public interface PhoneNumberService {
    void save(PhoneNumber phoneNumber);
    PhoneNumber findById(Integer id);
    List<PhoneNumber> findAll();
    void deleteById(Integer id);
    void update(PhoneNumber phoneNumber);
}
