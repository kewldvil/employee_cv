package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Skill;
import com.noc.employee_cv.models.UniversitySkill;

import java.util.List;

public interface SkillService {
    void save(Skill skill);
    Skill findById(Integer id);
    Skill findByName(String name);
    List<Skill> findAll();
    void deleteById(Integer id);
    void update(Skill skill);
}
