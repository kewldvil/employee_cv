package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepo extends JpaRepository<Language,Integer> {
    Language findByLanguage(String languageName);
}
