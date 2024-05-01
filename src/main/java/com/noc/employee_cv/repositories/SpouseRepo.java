package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Spouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpouseRepo extends JpaRepository<Spouse,Integer> {
}
