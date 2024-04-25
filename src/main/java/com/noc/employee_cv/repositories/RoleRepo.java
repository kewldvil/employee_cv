package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String role);
}
