package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String role);
}
