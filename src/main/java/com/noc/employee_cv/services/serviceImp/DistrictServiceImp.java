package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.repositories.DistrictRepo;
import com.noc.employee_cv.services.DistrictService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DistrictServiceImp implements DistrictService {
    private final DistrictRepo districtRepo;


    @Override
    public District getDistrictById(Integer id) {
        Optional<District> district = districtRepo.findById(id);
        return district.orElse(null);
    }

    @Override
    public List<District> getAllDistrictById(Integer id) {
        return districtRepo.findByProvince_city_id(id);
    }

}
