package com.noc.employee_cv.services;

import com.noc.employee_cv.model.Mother;
import com.noc.employee_cv.model.UniversitySkill;
import org.springframework.stereotype.Service;

import java.util.List;
public interface UniversitySkillService {
    void save(UniversitySkill universitySkill);
    UniversitySkill findById(Integer id);
    List<UniversitySkill> findAll();
    void deleteById(Integer id);
    void update(UniversitySkill universitySkill);

}
