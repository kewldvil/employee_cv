package com.noc.employee_cv.contollers;

import com.noc.employee_cv.authentication.AuthenticationService;
import com.noc.employee_cv.authentication.IncorrectPasswordException;
import com.noc.employee_cv.dto.PoliceRankCountProjection;
import com.noc.employee_cv.dto.UserEmployeeDTO;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImpl.EmployeeServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/v1/managements")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final EmployeeServiceImp employeeService;
    private final UserRepo userRepo;
    private final AuthenticationService service;
    @GetMapping("/total-employee")
    public ResponseEntity<Long> getTotalEmployees() {
        long totalEmployees = employeeService.getTotalEmployees();
        return ResponseEntity.ok(totalEmployees);
    }

    @GetMapping("/total-by-weapon")
    public ResponseEntity<Long> getTotalEmployeeUseWeapons() {
        long totalEmployees = employeeService.getTotalEmployeesByWeapon();
        return ResponseEntity.ok(totalEmployees);
    }
    @GetMapping("/total-by-police-car")
    public ResponseEntity<Long> getTotalEmployeeUsePoliceCars() {
        long totalEmployees = employeeService.getTotalEmployeesByPoliceCar();
        return ResponseEntity.ok(totalEmployees);
    }
    @GetMapping("/total-by-bachelor")
    public ResponseEntity<Long> getTotalEmployeeHaveBachelor() {
        long totalEmployees = employeeService.getTotalEmployeesByBachelor();
        return ResponseEntity.ok(totalEmployees);
    }
    @GetMapping("/total-by-master")
    public ResponseEntity<Long> getTotalEmployeeHaveMaster() {
        long totalEmployees = employeeService.getTotalEmployeesByMaster();
        return ResponseEntity.ok(totalEmployees);
    }
    @GetMapping("/total-by-doctor")
    public ResponseEntity<Long> getTotalEmployeeHaveDoctor() {
        long totalEmployees = employeeService.getTotalEmployeesByDoctor();
        return ResponseEntity.ok(totalEmployees);
    }
    @GetMapping("/total-by-female")
    public ResponseEntity<Long> getTotalFemales() {
        long totalEmployees = employeeService.getTotalFemales();
        return ResponseEntity.ok(totalEmployees);
    }
    @GetMapping("/count-by-police-rank")
    public List<PoliceRankCountProjection> getCountByPoliceRank() {
        System.out.println("getCountByPoliceRank");
        return employeeService.countByPoliceRanks();
    }
    @GetMapping("/count-by-trainee")
    public ResponseEntity<Long> getCountByTrainee() {
        System.out.println("getCountByTrainee");
        long totalTrainee = employeeService.getTotalTrainee();
        return ResponseEntity.ok(totalTrainee);
    }
    @GetMapping("/count-by-male-trainee")
    public ResponseEntity<Long> getCountByMaleTrainee() {
        System.out.println("getCountByTrainee");
        long totalTrainee = employeeService.getTotalMaleTrainee();
        return ResponseEntity.ok(totalTrainee);
    }
    @GetMapping("/count-by-female-trainee")
    public ResponseEntity<Long> getCountByFemaleTrainee() {
        System.out.println("getCountByTrainee");
        long totalTrainee = employeeService.getTotalFemaleTrainee();
        return ResponseEntity.ok(totalTrainee);
    }

    @GetMapping("/filter-users/{filter}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserEmployeeDTO>> listUsersByFilter(@PathVariable String filter) {
        System.out.println("GET USERS BY "+filter);
        List<User> users = switch (filter) {
            case "F" -> employeeService.finderUsersByGender(filter);
            case "WEAPON" -> employeeService.findAllUsersWithEmployeeAndWeapons();
            case "មន្ត្រីហាត់ការ" -> employeeService.findUserByTrainee(filter);
            case "POLICE_CAR" -> employeeService.findAllUsersWithEmployeeAndPoliceCar();
            case "BACHELOR" -> employeeService.findEmployeeAndUserByDegree(5);
            case "MASTER" -> employeeService.findEmployeeAndUserByDegree(3);
            case "DOCTOR" -> employeeService.findEmployeeAndUserByDegree(6);
            default -> userRepo.findUsersByRank(filter);
        };
        // Check if users list is not null or empty
        if (!users.isEmpty()) {
            // If users list is not empty, return it with HTTP status 200 OK
            List<UserEmployeeDTO> employees = users.stream().map(EmployeeController::convertToDTO).toList();
            return ResponseEntity.ok(employees);
        } else {
            // If users list is empty, return 404 Not Found status code
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/status/{userId}/{status}")
    public ResponseEntity<String> updateUserAccountStatus(
            @PathVariable Integer userId,
            @PathVariable boolean status) {
        System.out.println(userId);
        System.out.println(status);
        try {
            // Call the service to update the user's status
            service.updateUserByEnabled(userId, status);
            return ResponseEntity.ok("User status updated successfully.");
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(500).body("Error updating user status: " + e.getMessage());
        }
    }
    @PutMapping("/user/reset-password/{userId}")
    public ResponseEntity<String> resetUserPassword(@PathVariable Integer userId) {
        System.out.println("reset password");
        try {
            service.resetPassword(userId);
            return ResponseEntity.accepted().build();
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
    }
}
