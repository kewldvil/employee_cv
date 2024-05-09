package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.provinces.ProvinceCity;
import com.noc.employee_cv.repositories.ProvinceCityRepo;
import com.noc.employee_cv.services.ProvinceCityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProvinceCityServiceImp implements ProvinceCityService {
    private final ProvinceCityRepo provinceCityRepo;

    @Override
    public ProvinceCity getProvinceById(Integer id) {
        Optional<ProvinceCity> province = provinceCityRepo.findById(id);
        return province.orElse(null); // Return user if present; otherwise
    }

    @Override
    public List<ProvinceCity> getAllProvinceCities() {
        return provinceCityRepo.findAll();
    }

}
