package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Address;
import com.noc.employee_cv.repository.AddressRepo;
import com.noc.employee_cv.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {
    private final AddressRepo addressRepo;

    @Override
    @Transactional
    public void save(Address address) {
        addressRepo.save(address);
    }

    @Override
    public Address findById(Integer id) {
        return addressRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Address> findAll() {
        return addressRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        addressRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Address address) {
        addressRepo.save(address);
    }
}
