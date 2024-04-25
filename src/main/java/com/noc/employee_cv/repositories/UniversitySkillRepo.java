package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.UniversitySkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversitySkillRepo extends JpaRepository<UniversitySkill,Integer> {
}
