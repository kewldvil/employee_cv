package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.District;
import com.noc.employee_cv.repository.DistrictRepo;
import com.noc.employee_cv.services.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepo districtRepo;

    @Override
    public District getDistrictById(Integer id) {
        return districtRepo.findById(id).orElse(null);
    }

    @Override
    public List<District> getAllDistrictById(Integer id) {
        return districtRepo.findByProvinceCityIdAndEnabledTrue(id);
    }

    @Override
    @Transactional
    public District save(District district) {
        return districtRepo.save(district);
    }

    @Override
    @Transactional
    public void update(District district) {
        districtRepo.save(district);
    }

    public String getNextDistrictCode() {
        return districtRepo.findFirstByOrderByDistrict_codeDesc()
                .map(district -> String.valueOf(Integer.parseInt(district.getDistrict_code()) + 1))
                .orElse("1");
    }
}
