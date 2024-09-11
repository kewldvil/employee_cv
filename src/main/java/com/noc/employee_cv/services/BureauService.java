package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Bureau;
import com.noc.employee_cv.models.Department;

import java.util.List;

public interface BureauService {
    void save(Bureau bureau);
    Bureau findById(Integer id);
    List<Bureau> findAll();
    void deleteById(Integer id);
    void update(Bureau bureau);
}
