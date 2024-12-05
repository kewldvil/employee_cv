package com.noc.employee_cv.services;

import com.noc.employee_cv.provinces.District;

import java.util.List;

public interface DistrictService {
    District getDistrictById(Integer id);
    List<District> getAllDistrictById(Integer provinceId);
    void save(District district);
    void update(District district);


}
