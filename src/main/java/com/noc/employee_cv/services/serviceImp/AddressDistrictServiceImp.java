package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.AddressDistrict;
import com.noc.employee_cv.repositories.AddressDistrictRepo;
import com.noc.employee_cv.services.AddressDistrictService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AddressDistrictServiceImp implements AddressDistrictService {
    private final AddressDistrictRepo addressDistrictRepo;
    @Override
    public void save(AddressDistrict addressDistrict) {
        addressDistrictRepo.save(addressDistrict);
    }

    @Override
    public AddressDistrict findById(Integer id) {
        return null;
    }

    @Override
    public List<AddressDistrict> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(AddressDistrict addressDistrict) {

    }
}
