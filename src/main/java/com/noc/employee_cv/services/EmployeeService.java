package com.noc.employee_cv.services;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.PoliceRankCountProjection;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeService {
    void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType);
    Employee findById(Integer id);
    List<Employee> findAll();
    void deleteById(Integer id);
    void update(EmployeeDTO employeeDTO);
    void save(EmployeeDTO employeeDTO);
    Employee findByUserIdAndEmployeeId(Integer employeeId,Integer userId);

    long getTotalEmployees();
    long getTotalEmployeesByWeapon();
    long getTotalEmployeesByPoliceCar();
    long getTotalEmployeesByBachelor();
    long getTotalFemales();
    List<PoliceRankCountProjection> countByPoliceRanks();

     long getTotalTrainee();
    List<User> finderUsersByGender(String gender);

    List<User> findUsersByDynamicParameter(String parameter,String value);
    List<User> findAllUsersWithEmployeeAndWeapons();
    List<User> findAllUsersWithEmployeeAndPoliceCar();
    List<User> findEmployeeAndUserByDegree(int degreeLevelId);

    List<User> findUserByTrainee(String trainee);

    long getTotalMaleTrainee();

    long getTotalFemaleTrainee();
    List<Object[]> getEmployeeStatsByDepartment();
    Object[] getAllEmployeeStats();

}
