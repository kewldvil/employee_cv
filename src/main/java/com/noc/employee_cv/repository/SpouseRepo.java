package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Spouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpouseRepo extends JpaRepository<Spouse,Integer> {
    Spouse findByEmployeeId(Integer employeeId);
}
