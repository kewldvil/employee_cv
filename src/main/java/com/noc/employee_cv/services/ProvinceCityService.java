package com.noc.employee_cv.services;

import com.noc.employee_cv.model.GeneralDepartment;
import com.noc.employee_cv.model.ProvinceCity;

import java.util.List;

public interface ProvinceCityService {
    ProvinceCity getProvinceById(Integer id);

    List<ProvinceCity> getAllProvinceCities();

    void save(ProvinceCity provinceCity);

    void deleteById(Integer id);

    void update(ProvinceCity provinceCity);
}
