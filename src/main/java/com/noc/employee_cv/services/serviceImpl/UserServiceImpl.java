package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.UserRepo;
import com.noc.employee_cv.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    @Transactional
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepo.findById(id).orElseThrow();
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        userRepo.save(user);
    }

    @Override
    public Page<User> findAllActiveUser(String search, Pageable pageable) {
        return userRepo.findAllByNameOrUsername(search, pageable);
    }

    @Override
    public Page<User> findByDepartmentId(Integer departmentId, String search, Pageable pageable) {
        return userRepo.findByDepartmentIdAndSearch(departmentId, search, pageable);
    }

    @Override
    public Page<User> findAllUsersWithEmployeeAndWeaponsByDepartment(Integer departmentId, String search, Pageable pageable) {
        return userRepo.findAllUsersWithEmployeeAndWeaponsByDepartment(departmentId, search, pageable);
    }

    @Override
    public Page<User> findAllUsersWithEmployeeAndPoliceCarByDepartment(Integer departmentId, String search, Pageable pageable) {
        return userRepo.findAllUsersWithEmployeeAndPoliceCarByDepartment(departmentId, search, pageable);
    }
}
