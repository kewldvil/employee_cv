package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Skill;
import com.noc.employee_cv.repository.SkillRepo;
import com.noc.employee_cv.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillServiceImpl implements SkillService {
    private final SkillRepo skillRepo;

    @Override
    @Transactional
    public void save(Skill skill) {
        skillRepo.save(skill);
    }

    @Override
    public Skill findById(Integer id) {
        return skillRepo.findById(id).orElseThrow();
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
    @Transactional
    public void deleteById(Integer id) {
        skillRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Skill skill) {
        skillRepo.save(skill);
    }
}
