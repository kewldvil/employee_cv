package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
//    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
