package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.GeneralDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralDepartmentRepo extends JpaRepository<GeneralDepartment,Integer> {
    List<GeneralDepartment> findAllByEnabledTrue();
}
