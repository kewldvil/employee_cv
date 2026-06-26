package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.EmployeeUniversitySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSkillRepo extends JpaRepository<EmployeeUniversitySkill,Integer> {
}
