package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Appreciation;
import com.noc.employee_cv.models.PhoneNumber;

import java.util.List;

public interface AppreciationService {
    void save(Appreciation appreciation);
    Appreciation findById(Integer id);
    List<Appreciation> findAll();
    void deleteById(Integer id);
    void update(Appreciation appreciation);
}
