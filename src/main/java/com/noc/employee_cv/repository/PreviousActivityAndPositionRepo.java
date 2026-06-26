package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.PreviousActivityAndPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreviousActivityAndPositionRepo extends JpaRepository<PreviousActivityAndPosition,Integer> {
}
