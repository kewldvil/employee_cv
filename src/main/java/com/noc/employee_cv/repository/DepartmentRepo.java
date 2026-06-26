package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Department;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Integer> {
    List<Department> findAllByEnabledTrue();
    List<Department> findAllByGeneralDepartmentIdAndEnabledTrue(Integer id);
    @Modifying
    @Transactional
    @Query("UPDATE Department d SET d.enabled = false WHERE d.generalDepartment.id = :generalDepartmentId")
    int updateSetEnabledFalseWhereGeneralDepartmentId(@Param("generalDepartmentId") int generalDepartmentId);

}
