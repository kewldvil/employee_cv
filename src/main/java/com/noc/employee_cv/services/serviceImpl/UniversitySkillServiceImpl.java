package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.UniversitySkill;
import com.noc.employee_cv.repository.UniversitySkillRepo;
import com.noc.employee_cv.services.UniversitySkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UniversitySkillServiceImpl implements UniversitySkillService {

    private final UniversitySkillRepo universitySkillRepo;

    @Override
    @Transactional
    public void save(UniversitySkill universitySkill) {
        universitySkillRepo.save(universitySkill);
    }

    @Override
    public UniversitySkill findById(Integer id) {
        return universitySkillRepo.findById(id).orElseThrow();
    }

    @Override
    public List<UniversitySkill> findAll() {
        return universitySkillRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        universitySkillRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(UniversitySkill universitySkill) {
        universitySkillRepo.save(universitySkill);
    }
}
