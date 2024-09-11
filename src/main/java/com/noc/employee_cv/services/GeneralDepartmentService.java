package com.noc.employee_cv.services;

import com.noc.employee_cv.dto.GeneralDepartmentDTO;
import com.noc.employee_cv.models.GeneralDepartment;
import com.noc.employee_cv.models.SpouseChildren;

import java.util.List;

public interface GeneralDepartmentService {
    void save(GeneralDepartment generalDepartment);
    GeneralDepartment findById(Integer id);
    List<GeneralDepartment> findAll();
    void deleteById(Integer id);
    void update(GeneralDepartment generalDepartment);
}
