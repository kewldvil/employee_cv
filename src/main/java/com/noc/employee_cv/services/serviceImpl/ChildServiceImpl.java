package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.SpouseChildren;
import com.noc.employee_cv.repository.ChildRepo;
import com.noc.employee_cv.services.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildServiceImpl implements ChildService {
    private final ChildRepo childRepo;

    @Override
    @Transactional
    public void save(SpouseChildren spouseChildren) {
        childRepo.save(spouseChildren);
    }

    @Override
    public SpouseChildren findById(Integer id) {
        return childRepo.findById(id).orElseThrow();
    }

    @Override
    public List<SpouseChildren> findAll() {
        return childRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        childRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(SpouseChildren spouseChildren) {
        childRepo.save(spouseChildren);
    }
}
