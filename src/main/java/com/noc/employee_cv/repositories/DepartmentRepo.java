package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Integer> {
    List<Department> findAllByEnabledTrue();

}
