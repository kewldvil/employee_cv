package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Employee;

import java.util.List;

public interface EmployeeService {
    void save(Employee employee);
    Employee findById(Long id);
    List<Employee> findAll();
    void deleteById(Long id);
    void update(Employee employee);
}
