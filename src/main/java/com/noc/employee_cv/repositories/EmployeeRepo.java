package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Integer> {
    Employee findByUserId(Integer userId);

    Employee findByIdAndUserId( Integer employeeId,Integer userId);

    long count();
}
