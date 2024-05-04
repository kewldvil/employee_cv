package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.EmployeeLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeLanguageRepo extends JpaRepository<EmployeeLanguage,Integer> {
}
