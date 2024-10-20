package com.noc.employee_cv.services;

import com.noc.employee_cv.models.SpouseChildren;
import com.noc.employee_cv.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void save(User spouseChildren);
    User findById(Integer id);
    List<User> findAll();
    void deleteById(Integer id);
    void update(User spouseChildren);
    Page<User> findAllActiveUser(String search,Pageable pageable);
    Page<User> findByDepartmentId(Integer departmentId,String search,Pageable pageable);
    Page<User> findAllUsersWithEmployeeAndWeaponsByDepartment(Integer departmentId,String search,Pageable pageable);
    Page<User> findAllUsersWithEmployeeAndPoliceCarByDepartment(Integer departmentId,String search,Pageable pageable);

}
