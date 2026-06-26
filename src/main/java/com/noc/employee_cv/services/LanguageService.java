package com.noc.employee_cv.services;

import com.noc.employee_cv.model.Father;
import com.noc.employee_cv.model.Language;

import java.util.List;

public interface LanguageService {
    void save(Language language);
    Language findById(Integer id);
    List<Language> findAll();
    void deleteById(Integer id);
    void update(Language language);
}
