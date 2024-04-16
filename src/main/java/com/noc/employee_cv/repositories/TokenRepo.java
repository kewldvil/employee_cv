package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token,Integer> {
    Optional<Token> findByToken(String token);
}
