package com.noc.employee_cv.services;

import com.noc.employee_cv.model.EmployeeLanguage;

import java.util.List;

public interface EmployeeLanguageService {
    void save(EmployeeLanguage employeeLanguage);
    EmployeeLanguage findById(Integer id);
    List<EmployeeLanguage> findAll();
    void deleteById(Integer id);
    void update(EmployeeLanguage employeeLanguage);
}
