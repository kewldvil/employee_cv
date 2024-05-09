package com.noc.employee_cv.services;

import com.noc.employee_cv.models.AddressDistrict;
import com.noc.employee_cv.models.AddressProvinceCity;

import java.util.List;

public interface AddressDistrictService {
    void save(AddressDistrict addressDistrict);
    AddressDistrict findById(Integer id);
    List<AddressDistrict> findAll();
    void deleteById(Integer id);
    void update(AddressDistrict addressDistrict);
}
