package com.noc.employee_cv.services;

import com.noc.employee_cv.models.UniversitySkill;
import org.springframework.stereotype.Service;

import java.util.List;
public interface UniversitySkillService {
    List<UniversitySkill> findAll();
}
