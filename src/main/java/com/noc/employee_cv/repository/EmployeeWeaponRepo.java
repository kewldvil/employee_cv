package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.EmployeeWeapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWeaponRepo extends JpaRepository<EmployeeWeapon,Integer> {
}
