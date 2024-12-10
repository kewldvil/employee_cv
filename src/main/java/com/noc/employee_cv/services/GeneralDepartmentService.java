package com.noc.employee_cv.services;

import com.noc.employee_cv.models.GeneralDepartment;

import java.util.List;

public interface GeneralDepartmentService {
    GeneralDepartment save(GeneralDepartment generalDepartment);
    GeneralDepartment findById(Integer id);
    List<GeneralDepartment> findAll();
    void deleteById(Integer id);
    void update(GeneralDepartment generalDepartment);
}
