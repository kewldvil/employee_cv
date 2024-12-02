package com.noc.employee_cv.services;
import com.noc.employee_cv.models.Position;


import java.util.List;

public interface PositionService {
    void save(Position position);
    Position findById(Integer id);
    Position findByPosition(String position);
    List<Position> findAll();
    void deleteById(Integer id);
    void update(Position position);
}

