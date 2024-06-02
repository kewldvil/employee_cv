package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepo extends JpaRepository<Skill,Integer> {
    Skill findSkillBySkillName(String skillName);
}
