package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee,Integer> {


//    public void save(Employee employee);
//    public void delete(Employee employee);
//    public Employee findById(int employeeId);
//    public List<Employee> findAll();
//    public void update(Employee employee);
}
