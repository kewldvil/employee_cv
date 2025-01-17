package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    //    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    User findUserById(Integer id);

    // Custom query to find user by username, date of birth, and phone number
    @Query("SELECT u FROM User u JOIN u.employee e WHERE u.username = :username AND (" +
            "e.phoneNumber LIKE %:phoneNumber% OR " +
            "e.phoneNumber LIKE %:delimiterPhoneNumberStart OR " +
            "e.phoneNumber LIKE :phoneNumberDelimiterEnd%) AND " +
            "e.dateOfBirth = :dateOfBirth")
    Optional<User> findByUsernameAndPhoneNumberAndDateOfBirth(@Param("username") String username,
                                                              @Param("phoneNumber") String phoneNumber,
                                                              @Param("delimiterPhoneNumberStart") String delimiterPhoneNumberStart,
                                                              @Param("phoneNumberDelimiterEnd") String phoneNumberDelimiterEnd,
                                                              @Param("dateOfBirth") LocalDate dateOfBirth);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.employee e WHERE e.currentPoliceRank=:rank AND u.enabled=true")
    List<User> findUsersByRank(String rank);

    @Query("SELECT u FROM User u JOIN u.employee e WHERE e.gender=:gender AND u.enabled=true")
    List<User> findUsersByGender(String gender);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.employee e " +
            "LEFT JOIN FETCH e.weapons w " +
            "WHERE u.enabled = true AND " +
            "((w.weaponBrand IS NOT NULL AND w.weaponBrand != '' AND w.weaponBrand != 'គ្មាន') " +
            "   OR (w.weaponSerialNumber IS NOT NULL AND w.weaponSerialNumber != '' AND w.weaponSerialNumber != 'គ្មាន') " +
            "   OR (w.weaponType IS NOT NULL AND w.weaponType != '' AND w.weaponType != 'គ្មាន' AND w.weaponType != 'N/A'))")
    List<User> findAllUsersWithEmployeeAndWeapons();

    //    @Query("SELECT DISTINCT u FROM User u " +
//            "JOIN FETCH u.employee e " +
//            "LEFT JOIN FETCH e.weapons w " +
//            "WHERE u.enabled = true " +
//            "AND (:departmentId = 0 OR e.department.id = :departmentId) " +  // Allow all departments if departmentId == 0
//            "AND ( " +
//            "   (w.weaponBrand IS NOT NULL AND w.weaponBrand != '' AND w.weaponBrand != 'គ្មាន') " +
//            "   OR (w.weaponSerialNumber IS NOT NULL AND w.weaponSerialNumber != '' AND w.weaponSerialNumber != 'គ្មាន') " +
//            "   OR (w.weaponType IS NOT NULL AND w.weaponType != '' AND w.weaponType != 'គ្មាន' AND w.weaponType != 'N/A') " +
//            ") " +
//            "AND (CONCAT(u.lastname, ' ', u.firstname) LIKE %:search% OR u.username LIKE %:search%)")
//    Page<User> findAllUsersWithEmployeeAndWeaponsByDepartment(
//            @Param("departmentId") Integer departmentId,
//            @Param("search") String search,
//            Pageable pageable
//    );
    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.employee e " +
            "LEFT JOIN e.weapons w " +
            "WHERE u.enabled = true " +
            "AND (:departmentId = 0 OR e.department.id = :departmentId) " +
            "AND ( " +
            "   (w.weaponBrand IS NOT NULL AND w.weaponBrand != '' AND w.weaponBrand != 'គ្មាន') " +
            "   OR (w.weaponSerialNumber IS NOT NULL AND w.weaponSerialNumber != '' AND w.weaponSerialNumber != 'គ្មាន') " +
            "   OR (w.weaponType IS NOT NULL AND w.weaponType != '' AND w.weaponType != 'គ្មាន' AND w.weaponType != 'N/A') " +
            ") " +
            "AND (CONCAT(u.lastname, ' ', u.firstname) LIKE %:search% OR u.username LIKE %:search%)")
    Page<User> findAllUsersWithEmployeeAndWeaponsByDepartment(
            @Param("departmentId") Integer departmentId,
            @Param("search") String search,
            Pageable pageable
    );


    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.employee e " +
            "LEFT JOIN FETCH e.policePlateNumberCars p " +
            "WHERE u.enabled = true AND " +
            "(p.vehicleNumber != '') AND " +
            "(p.vehicleBrand != '')")
    List<User> findAllUsersWithEmployeeAndPoliceCar();

//    @Query("SELECT DISTINCT u FROM User u " +
//            "JOIN FETCH u.employee e " +
//            "LEFT JOIN FETCH e.policePlateNumberCars p " +
//            "WHERE u.enabled = true " +
//            "AND (:departmentId = 0 OR e.department.id = :departmentId) " +  // Allow all departments if departmentId == 0
//            "AND (p.vehicleNumber IS NOT NULL AND p.vehicleNumber <> '') " + // Ensure vehicleNumber is not null or empty
//            "AND (p.vehicleBrand IS NOT NULL AND p.vehicleBrand <> '') " +   // Ensure vehicleBrand is not null or empty
//            "AND (CONCAT(u.lastname, ' ', u.firstname) LIKE %:search% OR u.username LIKE %:search%)")
//    Page<User> findAllUsersWithEmployeeAndPoliceCarByDepartment(
//            @Param("departmentId") Integer departmentId,
//            @Param("search") String search,
//            Pageable pageable
//    );

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.employee e " +
            "LEFT JOIN e.policePlateNumberCars p " +
            "WHERE u.enabled = true " +
            "AND (:departmentId = 0 OR e.department.id = :departmentId) " +  // Allow all departments if departmentId == 0
            "AND (p.vehicleNumber IS NOT NULL AND p.vehicleNumber <> '') " + // Ensure vehicleNumber is not null or empty
            "AND (p.vehicleBrand IS NOT NULL AND p.vehicleBrand <> '') " +   // Ensure vehicleBrand is not null or empty
            "AND (CONCAT(u.lastname, ' ', u.firstname) LIKE %:search% OR u.username LIKE %:search%)")
    Page<User> findAllUsersWithEmployeeAndPoliceCarByDepartment(
            @Param("departmentId") Integer departmentId,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.employee e " +
            "JOIN FETCH e.employeeDegreeLevels ed " +
            "JOIN FETCH ed.degreeLevel dl " +
            "WHERE dl.id = :degreeLevelId AND ed.isChecked = true AND u.enabled=true")
    List<User> findEmployeeAndUserByDegree(@Param("degreeLevelId") Integer degreeLevelId);

    @Query("SELECT u FROM User u JOIN u.employee e WHERE e.currentPoliceRank=:trainee AND u.enabled=true")
    List<User> findUsersByTrainee(String trainee);

    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countEnabledUsers();

    @Modifying
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
    void updateUserByEnabled(Integer id, boolean enabled);

    boolean existsByUsername(String username);

    //    @Query("SELECT e.department.name, " +
//            "e.department.id, " +
//            "COUNT(DISTINCT u) AS totalUsers, " +  // Count distinct users to avoid duplicates
//            "SUM(CASE WHEN e.gender = 'M' AND e.currentPosition.position != 'មន្រ្តីហាត់ការ' THEN 1 ELSE 0 END) AS totalMales, " +
//            "SUM(CASE WHEN e.gender = 'F' AND e.currentPosition.position != 'មន្រ្តីហាត់ការ' THEN 1 ELSE 0 END) AS totalFemales, " +
//            "SUM(CASE WHEN e.currentPosition = 'មន្រ្តីហាត់ការ' AND e.gender = 'M' THEN 1 ELSE 0 END) AS totalMaleTrainees, " +
//            "SUM(CASE WHEN e.currentPosition = 'មន្រ្តីហាត់ការ' AND e.gender = 'F' THEN 1 ELSE 0 END) AS totalFemaleTrainees, " +
//            "SUM(CASE WHEN (w.id IS NOT NULL AND w.weaponBrand IS NOT NULL AND w.weaponBrand <> '' AND w.weaponBrand <> 'គ្មាន') " +
//            "OR (w.weaponSerialNumber IS NOT NULL AND w.weaponSerialNumber <> '' AND w.weaponSerialNumber <> 'គ្មាន') " +
//            "OR (w.weaponType IS NOT NULL AND w.weaponType <> '' AND w.weaponType <> 'គ្មាន' AND w.weaponType <> 'N/A') THEN 1 ELSE 0 END) AS totalEmployeesWithWeapons, " +
//            "SUM(CASE WHEN (p.vehicleBrand IS NOT NULL AND p.vehicleBrand <> '') " +
//            "AND (p.vehicleNumber IS NOT NULL AND p.vehicleNumber <> '') THEN 1 ELSE 0 END) AS totalEmployeesWithPoliceCars " +
//            "FROM User u " +
//            "LEFT JOIN u.employee e " +  // Join Employee entity
//            "LEFT JOIN e.weapons w " +   // Join Weapons entity (one-to-many)
//            "LEFT JOIN e.policePlateNumberCars p " +  // Join PoliceCars entity (one-to-many)
//            "WHERE u.enabled = true " +  // Filter for enabled users
//            "GROUP BY e.department.name, e.department.id " +
//            "ORDER BY e.department.id ASC")
//    List<Object[]> getUserStatsByDepartment();
    @Query("SELECT e.department.name, " +
            "e.department.id, " +
            "COUNT(DISTINCT u) AS totalUsers, " +  // Count distinct users to avoid duplicates
            "SUM(CASE WHEN e.gender = 'M' AND e.currentPosition.position != 'មន្រ្តីហាត់ការ' THEN 1 ELSE 0 END) AS totalMales, " +
            "SUM(CASE WHEN e.gender = 'F' AND e.currentPosition.position != 'មន្រ្តីហាត់ការ' THEN 1 ELSE 0 END) AS totalFemales, " +
            "SUM(CASE WHEN e.currentPosition.position = 'មន្រ្តីហាត់ការ' AND e.gender = 'M' THEN 1 ELSE 0 END) AS totalMaleTrainees, " +
            "SUM(CASE WHEN e.currentPosition.position = 'មន្រ្តីហាត់ការ' AND e.gender = 'F' THEN 1 ELSE 0 END) AS totalFemaleTrainees, " +
            "SUM(CASE WHEN EXISTS (SELECT 1 FROM Weapon w2 WHERE w2.employee.id = e.id " +
            "                      AND ((w2.weaponBrand IS NOT NULL AND w2.weaponBrand <> '' AND w2.weaponBrand <> 'គ្មាន') " +
            "                           OR (w2.weaponSerialNumber IS NOT NULL AND w2.weaponSerialNumber <> '' AND w2.weaponSerialNumber <> 'គ្មាន') " +
            "                           OR (w2.weaponType IS NOT NULL AND w2.weaponType <> '' AND w2.weaponType <> 'គ្មាន' AND w2.weaponType <> 'N/A'))) " +
            "THEN 1 ELSE 0 END) AS totalEmployeesWithWeapons, " +
            "SUM(CASE WHEN EXISTS (SELECT 1 FROM PolicePlateNumberCar p2 WHERE p2.employee.id = e.id " +
            "                      AND (p2.vehicleBrand IS NOT NULL AND p2.vehicleBrand <> '') " +
            "                      AND (p2.vehicleNumber IS NOT NULL AND p2.vehicleNumber <> '')) " +
            "THEN 1 ELSE 0 END) AS totalEmployeesWithPoliceCars " +
            "FROM User u " +
            "LEFT JOIN u.employee e " +  // Join Employee entity
            "WHERE u.enabled = true " +  // Filter for enabled users
            "GROUP BY e.department.name, e.department.id " +
            "ORDER BY e.department.id ASC")
    List<Object[]> getUserStatsByDepartment();


    @Query("SELECT  " +
            "COUNT(DISTINCT u), " +  // Count distinct users to avoid duplicates
            "SUM(CASE WHEN e.gender = 'M' AND e.currentPosition.position != 'មន្រ្តីហាត់ការ' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN e.gender = 'F' AND e.currentPosition.position != 'មន្រ្តីហាត់ការ' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN e.currentPosition.position = 'មន្រ្តីហាត់ការ' AND e.gender = 'M' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN e.currentPosition.position = 'មន្រ្តីហាត់ការ' AND e.gender = 'F' THEN 1 ELSE 0 END), " +
            "COUNT(DISTINCT e.id) FILTER (WHERE (w.id IS NOT NULL AND w.weaponBrand != '' AND w.weaponBrand != 'គ្មាន') OR " +
            "(w.weaponSerialNumber IS NOT NULL AND w.weaponSerialNumber != '' AND w.weaponSerialNumber != 'គ្មាន') OR " +
            "(w.weaponType IS NOT NULL AND w.weaponType != '' AND w.weaponType != 'គ្មាន' AND w.weaponType != 'N/A')), " +
            "COUNT(DISTINCT e.id) FILTER (WHERE p.vehicleBrand <> '' AND p.vehicleNumber <> '') " +
            "FROM User u " +
            "LEFT JOIN u.employee e " +  // Join Employee entity
            "LEFT JOIN e.weapons w " +  // Join Weapons entity (one-to-many)
            "LEFT JOIN e.policePlateNumberCars p " +  // Join PoliceCars entity (one-to-many)
            "WHERE u.enabled = true")
    Object[] getAllUserStats();


//    @Query("SELECT u FROM User u " +
//            "JOIN u.employee e " +
//            "LEFT JOIN e.currentPosition p " +
//            "WHERE e.department.id = :departmentId " +
//            "AND (u.lastname LIKE %:search% OR u.firstname LIKE %:search% OR u.username LIKE %:search%) " +
//            "ORDER BY COALESCE(p.sortOrder, 0) DESC")
//    Page<User> findByDepartmentIdAndSearch(
//            @Param("departmentId") Integer departmentId,
//            @Param("search") String search,
//            Pageable pageable
//    );


    //    @Query("SELECT u FROM User u " +
//            "LEFT JOIN u.employee e " +
//            "LEFT JOIN e.currentPosition p " +
//            "WHERE u.lastname LIKE %:search% OR u.firstname LIKE %:search% OR u.username LIKE %:search% " +
//            "ORDER BY COALESCE(p.sortOrder, 0) DESC")
//    Page<User> findAllByNameOrUsername(@Param("search") String search, Pageable pageable);
    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.employee e " +
            "LEFT JOIN e.currentPosition p " +
            "WHERE u.lastname LIKE %:search% OR u.firstname LIKE %:search% OR u.username LIKE %:search% " +
            "ORDER BY COALESCE(p.sortOrder, 0) DESC")
    Page<User> findAllByNameOrUsername(@Param("search") String search, Pageable pageable);



    @Query("SELECT u FROM User u " +
            "JOIN u.employee e " +
            "LEFT JOIN e.currentPosition p " +
            "WHERE e.department.id = :departmentId " +
            "AND (u.lastname LIKE %:search% OR u.firstname LIKE %:search% OR u.username LIKE %:search%) " +
            "ORDER BY COALESCE(p.sortOrder, 0) DESC")
    Page<User> findByDepartmentIdAndSearch(
            @Param("departmentId") Integer departmentId,
            @Param("search") String search,
            Pageable pageable
    );



}
