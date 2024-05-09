package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.AddressProvinceCity;
import com.noc.employee_cv.repositories.AddressProvinceCityRepo;
import com.noc.employee_cv.services.AddressProvinceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AddressProvinceServiceImp implements AddressProvinceService {
    private final AddressProvinceCityRepo addressProvinceCityRepo;
    @Override
    public void save(AddressProvinceCity addressProvinceCity) {
        addressProvinceCityRepo.save(addressProvinceCity);
    }

    @Override
    public AddressProvinceCity findById(Integer id) {
        return null;
    }

    @Override
    public List<AddressProvinceCity> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(AddressProvinceCity addressProvinceCity) {

    }
}
