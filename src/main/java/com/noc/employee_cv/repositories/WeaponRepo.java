package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeaponRepo extends JpaRepository<Weapon,Integer> {
}
