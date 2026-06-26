package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.VocationalTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocationalTrainingRepo extends JpaRepository<VocationalTraining,Integer> {
}
