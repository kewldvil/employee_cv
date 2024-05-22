package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.SpouseChildren;
import com.noc.employee_cv.repositories.ChildRepo;
import com.noc.employee_cv.services.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildServiceImp implements ChildService {
    private final ChildRepo childRepo;
    @Override
    public void save(SpouseChildren spouseChildren) {
        childRepo.save(spouseChildren);
    }

    @Override
    public SpouseChildren findById(Integer id) {
        return null;
    }

    @Override
    public List<SpouseChildren> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(SpouseChildren spouseChildren) {

    }
}
