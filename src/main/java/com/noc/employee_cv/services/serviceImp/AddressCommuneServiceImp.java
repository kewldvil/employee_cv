package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.AddressCommune;
import com.noc.employee_cv.repositories.AddressCommuneRepo;
import com.noc.employee_cv.repositories.AddressDistrictRepo;
import com.noc.employee_cv.services.AddressCommuneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressCommuneServiceImp implements AddressCommuneService {
private final AddressCommuneRepo addressCommuneRepo;
    @Override
    public void save(AddressCommune addressCommune) {
        addressCommuneRepo.save(addressCommune);
    }

    @Override
    public AddressCommune findById(Integer id) {
        return null;
    }

    @Override
    public List<AddressCommune> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(AddressCommune addressCommune) {

    }
}
