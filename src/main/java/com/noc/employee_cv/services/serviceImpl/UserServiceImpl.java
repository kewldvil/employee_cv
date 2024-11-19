package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void save(User spouseChildren) {

    }

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(User spouseChildren) {

    }

    @Override
    public Page<User> findAllActiveUser(String search,Pageable pageable) {
        return userRepo.findAllByNameOrUsername(search,pageable);
    }

    @Override
    public Page<User> findByDepartmentId(Integer departmentId,String search, Pageable pageable) {
        return userRepo.findByDepartmentIdAndSearch(departmentId,search, pageable);
    }

    @Override
    public Page<User> findAllUsersWithEmployeeAndWeaponsByDepartment(Integer departmentId,String search, Pageable pageable) {
        return userRepo.findAllUsersWithEmployeeAndWeaponsByDepartment(departmentId,search,pageable);
    }

    @Override
    public Page<User> findAllUsersWithEmployeeAndPoliceCarByDepartment(Integer departmentId, String search,Pageable pageable) {
        return userRepo.findAllUsersWithEmployeeAndPoliceCarByDepartment(departmentId,search,pageable);
    }



}
