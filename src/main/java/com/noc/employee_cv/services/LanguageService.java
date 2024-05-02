package com.noc.employee_cv.services;

import com.noc.employee_cv.models.Father;
import com.noc.employee_cv.models.Language;

import java.util.List;

public interface LanguageService {
    void save(Language language);
    Language findById(Integer id);
    List<Language> findAll();
    void deleteById(Integer id);
    void update(Language language);
}
