package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.EmployeeLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeLanguageRepo extends JpaRepository<EmployeeLanguage,Integer> {
    List<EmployeeLanguage> findByEmployeeId(Integer id);
}
