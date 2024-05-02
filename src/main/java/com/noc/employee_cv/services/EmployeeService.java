package com.noc.employee_cv.services;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.models.Employee;

import java.util.List;

public interface EmployeeService {
    void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType);
    void save(Employee employee);
    Employee findById(Integer id);
    List<Employee> findAll();
    void deleteById(Integer id);
    void update(Employee employee);
    void createNew(EmployeeDTO employeeDTO);
}
