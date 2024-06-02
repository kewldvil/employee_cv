package com.noc.employee_cv.services;

import com.noc.employee_cv.models.PolicePlateNumberCar;

import java.util.List;

public interface PolicePlateNumberCarService {
    void save(PolicePlateNumberCar policePlateNumberCar);
    PolicePlateNumberCar findById(Integer id);
    List<PolicePlateNumberCar> findAll();
    void deleteById(Integer id);
    void update(PolicePlateNumberCar policePlateNumberCar);
}
