package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.EmployeeLanguage;
import com.noc.employee_cv.repositories.EmployeeLanguageRepo;
import com.noc.employee_cv.services.EmployeeLanguageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeLanguageServiceImp implements EmployeeLanguageService {
    private final EmployeeLanguageRepo employeeLanguageRepo;
    @Override
    public void save(EmployeeLanguage employeeLanguage) {
        employeeLanguageRepo.save(employeeLanguage);
    }

    @Override
    public EmployeeLanguage findById(Integer id) {
        return null;
    }

    @Override
    public List<EmployeeLanguage> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(EmployeeLanguage employeeLanguage) {

    }
}
