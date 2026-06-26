package com.noc.employee_cv.services;

import com.noc.employee_cv.model.Bureau;
import com.noc.employee_cv.model.Department;

import java.util.List;

public interface BureauService {
    void save(Bureau bureau);
    Bureau findById(Integer id);
    List<Bureau> findAll();
    void deleteById(Integer id);
    void update(Bureau bureau);
}
