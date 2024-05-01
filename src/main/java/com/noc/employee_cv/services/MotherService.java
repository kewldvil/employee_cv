package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Mother;
import com.noc.employee_cv.models.PhoneNumber;

import java.util.List;

public interface MotherService {
    void save(Mother mother);
    Mother findById(Integer id);
    List<Mother> findAll();
    void deleteById(Integer id);
    void update(Mother mother);
}
