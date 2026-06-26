package com.noc.employee_cv.services;

import com.noc.employee_cv.model.Address;
import com.noc.employee_cv.model.EmployeeUniversitySkill;
import com.noc.employee_cv.model.UniversitySkill;

import java.util.List;

public interface EmployeeUniversitySkillService {
    void save(EmployeeUniversitySkill employeeUniversitySkill);
    EmployeeUniversitySkill findById(Integer id);
    List<EmployeeUniversitySkill> findAll();
    void deleteById(Integer id);
    void update(EmployeeUniversitySkill employeeUniversitySkill);
}
