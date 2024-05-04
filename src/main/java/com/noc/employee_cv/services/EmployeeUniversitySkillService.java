package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.EmployeeUniversitySkill;
import com.noc.employee_cv.models.UniversitySkill;

import java.util.List;

public interface EmployeeUniversitySkillService {
    void save(EmployeeUniversitySkill employeeUniversitySkill);
    EmployeeUniversitySkill findById(Integer id);
    List<EmployeeUniversitySkill> findAll();
    void deleteById(Integer id);
    void update(EmployeeUniversitySkill employeeUniversitySkill);
}
