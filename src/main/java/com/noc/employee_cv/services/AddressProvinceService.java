package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.AddressProvinceCity;

import java.util.List;

public interface AddressProvinceService {
    void save(AddressProvinceCity addressProvinceCity);
    AddressProvinceCity findById(Integer id);
    List<AddressProvinceCity> findAll();
    void deleteById(Integer id);
    void update(AddressProvinceCity addressProvinceCity);
}
