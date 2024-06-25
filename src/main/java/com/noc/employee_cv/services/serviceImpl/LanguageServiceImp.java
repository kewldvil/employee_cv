package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.Language;
import com.noc.employee_cv.repositories.LanguageRepo;
import com.noc.employee_cv.services.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LanguageServiceImp implements LanguageService {
    private final LanguageRepo languageRepo;
    @Override
    public void save(Language language) {
        languageRepo.save(language);
    }

    @Override
    public Language findById(Integer id) {
        return null;
    }

    @Override
    public List<Language> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Language language) {

    }
}
