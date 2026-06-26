package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.DegreeLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreeLevelRepo extends JpaRepository<DegreeLevel,Integer> {
    DegreeLevel findByEducationLevel(String educationLevel);
}
