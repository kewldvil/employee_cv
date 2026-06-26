package com.noc.employee_cv.services;

import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.model.Address;
import com.noc.employee_cv.model.Employee;

import java.util.List;

public interface AddressService {
    void save(Address address);
    Address findById(Integer id);
    List<Address> findAll();
    void deleteById(Integer id);
    void update(Address address);
}
