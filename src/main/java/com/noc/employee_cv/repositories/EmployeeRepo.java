package com.noc.employee_cv.repositories;

import com.noc.employee_cv.dto.PoliceRankCountProjection;
import com.noc.employee_cv.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    Employee findByUserId(Integer userId);

    Employee findByIdAndUserId(Integer employeeId, Integer userId);

    long count();

    //    @Query("SELECT COUNT(e) FROM Employee e JOIN e.weapons w")
//    long countEmployeesWithWeapons();
    @Query("SELECT COUNT(DISTINCT e) FROM Employee e " +
            "JOIN e.weapons w " +
            "JOIN e.user u " +
            "WHERE u.enabled = true AND (" +
            "(w.weaponBrand IS NOT NULL AND w.weaponBrand != '' AND w.weaponBrand != 'គ្មាន') " +
            "OR (w.weaponSerialNumber IS NOT NULL AND w.weaponSerialNumber != '' AND w.weaponSerialNumber != 'គ្មាន') " +
            "OR (w.weaponType IS NOT NULL AND w.weaponType != '' AND w.weaponType != 'គ្មាន' AND w.weaponType != 'N/A'))")
    long countEmployeesWithWeapons();

    @Query("SELECT COUNT(DISTINCT e) FROM Employee e " +
            "JOIN e.policePlateNumberCars p " +
            "JOIN e.user u " +
            "WHERE u.enabled = true AND p.vehicleBrand <> '' AND p.vehicleNumber <> ''")
    long countEmployeesWithPoliceCars();


//    @Query("SELECT COUNT(DISTINCT e) FROM Employee e JOIN e.employeeDegreeLevels edl WHERE edl.degreeLevel = 'BACHELOR'")
//    long countEmployeesWithBachelorDegree();

    @Query(value = "SELECT COUNT(DISTINCT e.id) " +
            "FROM employee e " +
            "INNER JOIN _user u ON e.user_id = u.id " +
            "INNER JOIN employee_degree_level edl ON e.id = edl.employee_id " +
            "INNER JOIN degree_level dl ON edl.degree_level_id = dl.id " +
            "WHERE dl.id = :id AND edl.is_checked = true AND u.enabled = true",
            nativeQuery = true)
    long countEmployeesWithDegreeLevelChecked(@Param("id") Integer degreeLevelId);


    //    @Query("SELECT COUNT(e) FROM Employee e WHERE e.gender = 'F'")
//    long countFemaleEmployees();
    @Query("SELECT COUNT(e) FROM Employee e " +
            "JOIN e.user u " +
            "WHERE e.gender = 'F' AND e.currentPoliceRank != 'មន្ត្រីហាត់ការ' AND u.enabled = true")
    long countFemaleEmployees();


    @Query(value = "SELECT e.current_police_rank AS policeRank, COUNT(e.id) AS count " +
            "FROM employee e " +
            "INNER JOIN _user u ON e.user_id = u.id " +
            "WHERE u.enabled = true " +
            "GROUP BY e.current_police_rank", nativeQuery = true)
    List<PoliceRankCountProjection> countEmployeesByPoliceRank();


    @Query("SELECT COUNT(e) FROM Employee e JOIN e.user u WHERE e.currentPoliceRank = 'មន្ត្រីហាត់ការ' AND u.enabled =true")
    long countEmployeesByTrainee();

    @Query("SELECT COUNT(e) FROM Employee e JOIN e.user u WHERE e.currentPoliceRank = 'មន្ត្រីហាត់ការ' AND e.gender='M' AND u.enabled=true")
    long countEmployeesByMaleTrainee();

    @Query("SELECT COUNT(e) FROM Employee e JOIN e.user u WHERE e.currentPoliceRank = 'មន្ត្រីហាត់ការ' AND e.gender='F' AND u.enabled=true")
    long countEmployeesByFemaleTrainee();

    @Query("SELECT e,u.imageName FROM Employee e JOIN e.user u WHERE e.id = :id")
    Employee findEmployeeAndUserById(@Param("id") Integer id);


}
