package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.DegreeLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreeLevelRepo extends JpaRepository<DegreeLevel,Integer> {
    DegreeLevel findByEducationLevel(String educationLevel);
}
