package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.ProvinceCity;
import com.noc.employee_cv.repository.ProvinceCityRepo;
import com.noc.employee_cv.services.ProvinceCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvinceCityServiceImpl implements ProvinceCityService {
    private final ProvinceCityRepo provinceCityRepo;

    @Override
    public ProvinceCity getProvinceById(Integer id) {
        return provinceCityRepo.findById(id).orElse(null);
    }

    @Override
    public List<ProvinceCity> getAllProvinceCities() {
        return provinceCityRepo.findAll();
    }

    @Override
    @Transactional
    public void save(ProvinceCity provinceCity) {
        provinceCityRepo.save(provinceCity);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        provinceCityRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(ProvinceCity provinceCity) {
        provinceCityRepo.save(provinceCity);
    }
}
