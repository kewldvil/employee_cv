package com.noc.employee_cv.services;

import com.noc.employee_cv.models.AddressDistrict;
import com.noc.employee_cv.models.AddressVillage;

import java.util.List;

public interface AddressVillageService {
    void save(AddressVillage addressVillage);
    AddressVillage findById(Integer id);
    List<AddressVillage> findAll();
    void deleteById(Integer id);
    void update(AddressVillage addressVillage);
}
