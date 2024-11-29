package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.Skill;
import com.noc.employee_cv.repositories.SkillRepo;
import com.noc.employee_cv.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepo skillRepo;
    @Override
    public void save(Skill skill) {
        skillRepo.save(skill);
    }

    @Override
    public Skill findById(Integer id) {
        return null;
    }

    @Override
    public Skill findByName(String name) {
        return skillRepo.findSkillBySkillName(name);
    }

    @Override
    public List<Skill> findAll() {
        return skillRepo.findAllByEnabledTrue();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Skill skill) {
        skillRepo.save(skill);
    }
}
