package com.noc.employee_cv.services;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.Father;
import com.noc.employee_cv.models.SpouseChildren;

import java.util.List;

public interface ChildService {
    void save(SpouseChildren spouseChildren);
    SpouseChildren findById(Integer id);
    List<SpouseChildren> findAll();
    void deleteById(Integer id);
    void update(SpouseChildren spouseChildren);
}
