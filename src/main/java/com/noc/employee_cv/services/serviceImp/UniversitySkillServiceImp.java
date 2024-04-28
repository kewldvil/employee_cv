package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.UniversitySkill;
import com.noc.employee_cv.repositories.UniversitySkillRepo;
import com.noc.employee_cv.services.UniversitySkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversitySkillServiceImp implements UniversitySkillService {

    private final UniversitySkillRepo universitySkillRepo;

    @Override
    public List<UniversitySkill> findAll() {
        return universitySkillRepo.findAll();
    }
}
