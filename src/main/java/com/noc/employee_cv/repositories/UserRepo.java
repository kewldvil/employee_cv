package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
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

}
