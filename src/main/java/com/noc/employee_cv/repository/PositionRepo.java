package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepo extends JpaRepository<Position,Integer> {
    List<Position> findAllByEnabledTrue();
    Position findByPosition(String position);
}