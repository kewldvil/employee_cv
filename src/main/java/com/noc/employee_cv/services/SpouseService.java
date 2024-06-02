package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Spouse;

import java.util.List;

public interface SpouseService {
    void save(Spouse spouse);
    Spouse findById(Integer id);
    List<Spouse> findAll();
    void deleteById(Integer id);
    void update(Spouse spouse);
}
