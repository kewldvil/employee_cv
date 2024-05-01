package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.EmployeeAddress;
import com.noc.employee_cv.repositories.AddressRepo;
import com.noc.employee_cv.repositories.EmployeeAddressRepo;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.services.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final AddressRepo addressRepo;
    private final EmployeeAddressRepo employeeAddressRepo;

    @Override
    @Transactional
    public void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow();
        Address address = addressRepo.findById(addressId).orElseThrow();

        EmployeeAddress employeeAddress= new EmployeeAddress();
        employeeAddress.setEmployee(employee);
        employeeAddress.setAddress(address);
        employeeAddress.setAddressType(addressType);

        employee.getEmployeeAddresses().add(employeeAddress);
        address.getEmployeeAddresses().add(employeeAddress);

        employeeAddressRepo.save(employeeAddress);
    }

    @Override
    public void save(Employee employee) {
        employeeRepo.save(employee);
    }

    @Override
    public Employee findById(Integer id) {
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Employee employee) {

    }
}
