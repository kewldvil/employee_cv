package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Department;
import com.noc.employee_cv.repository.DepartmentRepo;
import com.noc.employee_cv.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepo departmentRepo;

    @Override
    @Transactional
    public Department save(Department department) {
        return departmentRepo.save(department);
    }

    @Override
    public Department findById(Integer id) {
        return departmentRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Department> findAll() {
        return departmentRepo.findAllByEnabledTrue();
    }

    @Override
    public List<Department> findAllByGeneralDepartmentId(Integer id) {
        return departmentRepo.findAllByGeneralDepartmentIdAndEnabledTrue(id);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        departmentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Department department) {
        departmentRepo.save(department);
    }

    @Override
    @Transactional
    public int disableDepartmentsByGeneralDepartmentId(Integer id) {
        return departmentRepo.updateSetEnabledFalseWhereGeneralDepartmentId(id);
    }
}
