package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.VocationalTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocationalTrainingRepo extends JpaRepository<VocationalTraining,Integer> {
}
