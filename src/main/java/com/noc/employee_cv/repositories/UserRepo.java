package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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



    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.employee e " +
            "LEFT JOIN FETCH e.policePlateNumberCars p " +
            "WHERE u.enabled = true AND " +
            "(p.vehicleNumber != '') AND " +
            "(p.vehicleBrand != '')")
    List<User> findAllUsersWithEmployeeAndPoliceCar();


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
}
