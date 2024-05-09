package com.noc.employee_cv.services;

import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.Village;

import java.util.List;

public interface VillageService {
    Village getVillageById(Integer id);
    List<Village> getAllVillageById(Integer communeId);
}
