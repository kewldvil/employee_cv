package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.EmployeeWeapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWeaponRepo extends JpaRepository<EmployeeWeapon,Integer> {
}
