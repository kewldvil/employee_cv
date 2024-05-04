package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.repositories.AddressRepo;
import com.noc.employee_cv.services.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressServiceImp implements AddressService {
    private final AddressRepo addressRepo;
    @Override
    public void save(Address address) {
        addressRepo.save(address);
    }

    @Override
    public Address findById(Integer id) {
        return null;
    }

    @Override
    public List<Address> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Address address) {

    }
}
