package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.PreviousActivityAndPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreviousActivityAndPositionRepo extends JpaRepository<PreviousActivityAndPosition,Integer> {
}
