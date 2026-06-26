package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.EmployeeAddress;
import com.noc.employee_cv.repository.EmployeeAddressRepo;
import com.noc.employee_cv.services.EmployeeAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeAddressServiceImpl implements EmployeeAddressService {
    private final EmployeeAddressRepo employeeAddressRepo;

    @Override
    @Transactional
    public void save(EmployeeAddress employeeAddress) {
        employeeAddressRepo.save(employeeAddress);
    }

    @Override
    public EmployeeAddress findById(Integer id) {
        return employeeAddressRepo.findById(id).orElseThrow();
    }

    @Override
    public List<EmployeeAddress> findAll() {
        return employeeAddressRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        employeeAddressRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(EmployeeAddress employeeAddress) {
        employeeAddressRepo.save(employeeAddress);
    }
}
