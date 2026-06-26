package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.EmployeeUniversitySkill;
import com.noc.employee_cv.repository.EmployeeSkillRepo;
import com.noc.employee_cv.services.EmployeeUniversitySkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeUniversitySkillServiceImpl implements EmployeeUniversitySkillService {
    private final EmployeeSkillRepo employeeSkillRepo;

    @Override
    @Transactional
    public void save(EmployeeUniversitySkill employeeUniversitySkill) {
        employeeSkillRepo.save(employeeUniversitySkill);
    }

    @Override
    public EmployeeUniversitySkill findById(Integer id) {
        return employeeSkillRepo.findById(id).orElseThrow();
    }

    @Override
    public List<EmployeeUniversitySkill> findAll() {
        return employeeSkillRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        employeeSkillRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(EmployeeUniversitySkill employeeUniversitySkill) {
        employeeSkillRepo.save(employeeUniversitySkill);
    }
}
