package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeImp implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    @Override
    public void save(Employee employee) {
        System.out.println(employee.getDateOfBirth());
        employeeRepo.save(employee);
    }

    @Override
    public Employee findById(Long id) {
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(Employee employee) {

    }
}
