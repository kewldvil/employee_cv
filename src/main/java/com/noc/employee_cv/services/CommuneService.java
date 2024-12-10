package com.noc.employee_cv.services;

import com.noc.employee_cv.provinces.Commune;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommuneService {
    Commune getCommuneById(Integer id);
    List<Commune> getAllCommuneById(Integer districtId);
    Commune save(Commune commune);
    void update(Commune commune);
    List<Commune> disableAndRetrieveCommunesByDistrictId(int districtId);
}
