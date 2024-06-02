package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.EmployeeUniversitySkill;
import com.noc.employee_cv.repositories.EmployeeSkillRepo;
import com.noc.employee_cv.services.EmployeeUniversitySkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeUniversitySkillServiceImp implements EmployeeUniversitySkillService {
    private final EmployeeSkillRepo employeeSkillRepo;

    @Override
    public void save(EmployeeUniversitySkill employeeUniversitySkill) {
        employeeSkillRepo.save(employeeUniversitySkill);
    }

    @Override
    public EmployeeUniversitySkill findById(Integer id) {
        return null;
    }

    @Override
    public List<EmployeeUniversitySkill> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(EmployeeUniversitySkill employeeUniversitySkill) {

    }
}
