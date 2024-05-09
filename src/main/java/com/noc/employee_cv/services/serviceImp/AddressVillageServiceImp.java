package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.AddressVillage;
import com.noc.employee_cv.repositories.AddressVillageRepo;
import com.noc.employee_cv.services.AddressVillageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressVillageServiceImp implements AddressVillageService {
    private final AddressVillageRepo addressVillageRepo;
    @Override
    public void save(AddressVillage addressVillage) {
        addressVillageRepo.save(addressVillage);
    }

    @Override
    public AddressVillage findById(Integer id) {
        return null;
    }

    @Override
    public List<AddressVillage> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(AddressVillage addressVillage) {

    }
}
