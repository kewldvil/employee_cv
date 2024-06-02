package com.noc.employee_cv.services;

import com.noc.employee_cv.models.EmployeeWeapon;

import java.util.List;

public interface EmployeeWeaponService {
    void save(EmployeeWeapon employeeWeapon);
    EmployeeWeapon findById(Integer id);
    List<EmployeeWeapon> findAll();
    void deleteById(Integer id);
    void update(EmployeeWeapon employeeWeapon);
}
