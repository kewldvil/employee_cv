package com.noc.employee_cv.services;

import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;

import java.util.List;

public interface CommuneService {
    Commune getCommuneById(Integer id);
    List<Commune> getAllCommuneById(Integer districtId);
}
