package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.EmployeeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAddressRepo extends JpaRepository<EmployeeAddress, Integer> {
}
