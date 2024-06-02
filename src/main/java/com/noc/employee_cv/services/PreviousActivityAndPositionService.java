package com.noc.employee_cv.services;

import com.noc.employee_cv.models.PreviousActivityAndPosition;

import java.util.List;

public interface PreviousActivityAndPositionService {
    void save(PreviousActivityAndPosition previousActivityAndPosition);
    PreviousActivityAndPosition findById(Integer id);
    List<PreviousActivityAndPosition> findAll();
    void deleteById(Integer id);
    void update(PreviousActivityAndPosition previousActivityAndPosition);
}
