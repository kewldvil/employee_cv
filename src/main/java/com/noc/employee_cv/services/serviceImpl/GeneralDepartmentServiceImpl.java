package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.GeneralDepartment;
import com.noc.employee_cv.repository.GeneralDepartmentRepo;
import com.noc.employee_cv.services.GeneralDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeneralDepartmentServiceImpl implements GeneralDepartmentService {
    private final GeneralDepartmentRepo generalDepartmentRepo;

    @Override
    @Transactional
    public GeneralDepartment save(GeneralDepartment generalDepartment) {
        return generalDepartmentRepo.save(generalDepartment);
    }

    @Override
    public GeneralDepartment findById(Integer id) {
        return generalDepartmentRepo.findById(id).orElseThrow();
    }

    @Override
    public List<GeneralDepartment> findAll() {
        return generalDepartmentRepo.findAllByEnabledTrue();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        generalDepartmentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(GeneralDepartment generalDepartment) {
        generalDepartmentRepo.save(generalDepartment);
    }
}
