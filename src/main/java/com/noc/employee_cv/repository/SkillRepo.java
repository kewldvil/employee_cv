package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepo extends JpaRepository<Skill,Integer> {
    Skill findSkillBySkillName(String skillName);
    List<Skill> findAllByEnabledTrue();
}
