package com.noc.employee_cv.services;

import com.noc.employee_cv.model.Commune;
import com.noc.employee_cv.model.District;
import com.noc.employee_cv.model.Village;

import java.util.List;

public interface VillageService {
    Village getVillageById(Integer id);
    List<Village> getAllVillageById(Integer communeId);
    void save(Village village);
    void update(Village village);
    int disableVillageByCommuneId(int communeId);
}
