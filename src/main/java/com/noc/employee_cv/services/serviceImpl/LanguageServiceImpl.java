package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Language;
import com.noc.employee_cv.repository.LanguageRepo;
import com.noc.employee_cv.services.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepo languageRepo;

    @Override
    @Transactional
    public void save(Language language) {
        languageRepo.save(language);
    }

    @Override
    public Language findById(Integer id) {
        return languageRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Language> findAll() {
        return languageRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        languageRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Language language) {
        languageRepo.save(language);
    }
}
