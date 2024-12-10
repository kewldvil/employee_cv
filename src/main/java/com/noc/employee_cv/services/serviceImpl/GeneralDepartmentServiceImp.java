package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.GeneralDepartment;
import com.noc.employee_cv.repositories.GeneralDepartmentRepo;
import com.noc.employee_cv.services.GeneralDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GeneralDepartmentServiceImp implements GeneralDepartmentService {
    private final GeneralDepartmentRepo generalDepartmentRepo;
    @Override
    public GeneralDepartment save(GeneralDepartment generalDepartment) {
        generalDepartmentRepo.save(generalDepartment);
        return generalDepartment;
    }

    @Override
    public GeneralDepartment findById(Integer id) {
        return null;
    }

    @Override
    public List<GeneralDepartment> findAll() {
        return generalDepartmentRepo.findAllByEnabledTrue();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(GeneralDepartment generalDepartment) {
        generalDepartmentRepo.save(generalDepartment);
    }


}
