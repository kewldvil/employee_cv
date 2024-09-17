package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Department;

import java.util.List;

public interface DepartmentService {
    void save(Department department);
    Department findById(Integer id);
    List<Department> findAll();
    List<Department> findAllByGeneralDepartmentId(Integer id);
    void deleteById(Integer id);
    void update(Department department);

}
