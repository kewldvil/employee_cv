package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.Department;
import com.noc.employee_cv.repositories.DepartmentRepo;
import com.noc.employee_cv.services.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class DepartmentServiceImp implements DepartmentService {
    private final DepartmentRepo departmentRepo;
    @Override
    public void save(Department department) {
        departmentRepo.save(department);
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
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Department department) {
        departmentRepo.save(department);
    }
}
