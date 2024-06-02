package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Father;

import java.util.List;

public interface FatherService {
    void save(Father father);
    Father findById(Integer id);
    List<Father> findAll();
    void deleteById(Integer id);
    void update(Father father);
}
