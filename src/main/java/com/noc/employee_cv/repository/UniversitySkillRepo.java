package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.UniversitySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversitySkillRepo extends JpaRepository<UniversitySkill,Integer> {
}
