package com.noc.employee_cv.services;

import com.noc.employee_cv.provinces.ProvinceCity;

import java.util.List;

public interface ProvinceCityService {
    ProvinceCity getProvinceById(int id);
   List<ProvinceCity> getAllProvinceCities();
}
