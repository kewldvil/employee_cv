package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.UniversitySkill;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.repositories.UniversitySkillRepo;
import com.noc.employee_cv.services.UniversitySkillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversitySkillServiceImp implements UniversitySkillService {

    private final UniversitySkillRepo universitySkillRepo;
    private final EmployeeRepo employeeRepo;

    @Override
    public void save(UniversitySkill universitySkill) {
        universitySkillRepo.save(universitySkill);
    }

    @Override
    public UniversitySkill findById(Integer id) {
        return null;
    }

    @Override
    public List<UniversitySkill> findAll() {
        return universitySkillRepo.findAll();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(UniversitySkill universitySkill) {

    }


}
