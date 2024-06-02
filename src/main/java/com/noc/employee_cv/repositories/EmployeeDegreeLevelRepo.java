package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.EmployeeDegreeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDegreeLevelRepo extends JpaRepository<EmployeeDegreeLevel,Integer> {
}
