package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.GeneralDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralDepartmentRepo extends JpaRepository<GeneralDepartment,Integer> {
    List<GeneralDepartment> findAllByEnabledTrue();
}
