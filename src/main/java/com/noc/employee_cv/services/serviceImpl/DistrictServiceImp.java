package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.repositories.DistrictRepo;
import com.noc.employee_cv.services.DistrictService;
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
        return districtRepo.findByProvinceCityIdAndEnabledTrue(id);
    }

    @Override
    public void save(District district) {
        districtRepo.save(district);
    }

    @Override
    public void update(District district) {
        districtRepo.save(district);
    }
    public String getNextDistrictCode() {
        return districtRepo.findFirstByOrderByDistrict_codeDesc()
                .map(district -> String.valueOf(Integer.parseInt(district.getDistrict_code()) + 1))
                .orElse("1"); // Default to "1" if no record exists
    }
}
