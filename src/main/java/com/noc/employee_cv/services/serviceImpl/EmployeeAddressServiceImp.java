package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.EmployeeAddress;
import com.noc.employee_cv.repositories.EmployeeAddressRepo;
import com.noc.employee_cv.services.EmployeeAddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class EmployeeAddressServiceImp implements EmployeeAddressService {
    private final EmployeeAddressRepo employeeAddressRepo;

    @Override
    public void save(EmployeeAddress employeeAddress) {
        employeeAddressRepo.save(employeeAddress);
    }

    @Override
    public EmployeeAddress findById(Integer id) {
        return null;
    }

    @Override
    public List<EmployeeAddress> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(EmployeeAddress employeeAddress) {

    }
}
