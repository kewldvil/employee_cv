package com.noc.employee_cv.services;

import com.noc.employee_cv.models.AddressCommune;
import com.noc.employee_cv.models.AddressDistrict;

import java.util.List;

public interface AddressCommuneService {
    void save(AddressCommune addressCommune);
    AddressCommune findById(Integer id);
    List<AddressCommune> findAll();
    void deleteById(Integer id);
    void update(AddressCommune addressCommune);
}
