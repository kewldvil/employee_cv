package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.EmployeeDegreeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDegreeLevelRepo extends JpaRepository<EmployeeDegreeLevel,Integer> {
}
