package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.EmployeeLanguage;
import com.noc.employee_cv.repository.EmployeeLanguageRepo;
import com.noc.employee_cv.services.EmployeeLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeLanguageServiceImpl implements EmployeeLanguageService {
    private final EmployeeLanguageRepo employeeLanguageRepo;

    @Override
    @Transactional
    public void save(EmployeeLanguage employeeLanguage) {
        employeeLanguageRepo.save(employeeLanguage);
    }

    @Override
    public EmployeeLanguage findById(Integer id) {
        return employeeLanguageRepo.findById(id).orElseThrow();
    }

    @Override
    public List<EmployeeLanguage> findAll() {
        return employeeLanguageRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        employeeLanguageRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(EmployeeLanguage employeeLanguage) {
        employeeLanguageRepo.save(employeeLanguage);
    }
}
