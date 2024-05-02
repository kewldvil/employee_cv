package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepo extends JpaRepository<Language,Integer> {
}
