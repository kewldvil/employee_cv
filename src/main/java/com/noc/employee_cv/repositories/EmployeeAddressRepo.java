package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.EmployeeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAddressRepo extends JpaRepository<EmployeeAddress, Integer> {
}
