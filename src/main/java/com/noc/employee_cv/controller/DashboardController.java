package com.noc.employee_cv.controller;

import com.noc.employee_cv.authentication.AuthenticationService;
import com.noc.employee_cv.authentication.IncorrectPasswordException;
import com.noc.employee_cv.dto.PoliceRankCountProjection;
import com.noc.employee_cv.dto.UserEmployeeDTO;
import com.noc.employee_cv.dto.UserProfileDTO;
import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.model.Department;
import com.noc.employee_cv.model.Employee;
import com.noc.employee_cv.model.Skill;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.DepartmentRepo;
import com.noc.employee_cv.repository.EmployeeRepo;
import com.noc.employee_cv.repository.UserRepo;
import com.noc.employee_cv.services.serviceImpl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/managements")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final EmployeeServiceImpl employeeService;
    private final UserRepo userRepo;
    private final AuthenticationService service;
    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;

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
        return employeeService.countByPoliceRanks();
    }

    @GetMapping("/count-by-trainee")
    public ResponseEntity<Long> getCountByTrainee() {
        long totalTrainee = employeeService.getTotalTrainee();
        return ResponseEntity.ok(totalTrainee);
    }

    @GetMapping("/count-by-male-trainee")
    public ResponseEntity<Long> getCountByMaleTrainee() {
        long totalTrainee = employeeService.getTotalMaleTrainee();
        return ResponseEntity.ok(totalTrainee);
    }

    @GetMapping("/count-by-female-trainee")
    public ResponseEntity<Long> getCountByFemaleTrainee() {
        long totalTrainee = employeeService.getTotalFemaleTrainee();
        return ResponseEntity.ok(totalTrainee);
    }

    @GetMapping("/filter-users/{filter}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserEmployeeDTO>> listUsersByFilter(@PathVariable String filter) {
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
        try {
            service.updateUserByEnabled(userId, status);
            return ResponseEntity.accepted().body("User status updated successfully.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            log.error("Failed to update user status for userId={}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user status");
        }
    }

    @PutMapping("/user/reset-password/{userId}")
    public ResponseEntity<String> resetUserPassword(@PathVariable Integer userId) {
        try {
            String temporaryPassword = service.resetPassword(userId);
            return ResponseEntity.accepted().body(temporaryPassword);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
    }

    @PutMapping("/user/profile/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Integer userId, @RequestBody UserProfileDTO userProfile) {
        try {
            // Find the user by userId, or throw an exception if not found
            User user = userRepo.findUserById(userId);

            // Update the user details
            user.setFirstname(userProfile.getFirstname());
            user.setLastname(userProfile.getLastname());
            user.setEmail(userProfile.getEmail());
            user.setUsername(userProfile.getUsername());

            try {
                // Ensure role is valid
                user.setRole(Role.valueOf(userProfile.getRole()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role specified");
            }

            // Save the updated user
            userRepo.save(user);

            // Return 202 Accepted response
            return ResponseEntity.accepted().body("User profile updated successfully");

        } catch (ResponseStatusException e) {
            // Handle specific errors, like "User not found"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            // Handle any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating user profile");
        }
    }

    @PutMapping("/user/department/{userId}/{departmentId}")
    public ResponseEntity<String> changeUserDepartment(@PathVariable Integer userId, @PathVariable Integer departmentId) {
        try {


            Optional<User> optionalUser = Optional.ofNullable(userRepo.findUserById(userId));
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Optional<Employee> optionalEmployee = Optional.ofNullable(employeeRepo.findByUserId(optionalUser.get().getId()));
            if (optionalEmployee.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
            }
            Employee employee = optionalEmployee.get();
            Department department = departmentRepo.findById(departmentId)
                    .orElseThrow(() -> new NoSuchElementException("Department not found"));

            employee.setDepartment(department);
            employeeRepo.save(employee);
            return ResponseEntity.accepted().body("Department changed successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing department");
        }
    }


    @GetMapping("employee-stats-by-department")
    public ResponseEntity<List<Object[]>> getEmployeeState() {
        return ResponseEntity.ok(userRepo.getUserStatsByDepartment());
    }

    @GetMapping("employee-stats")
    public ResponseEntity<Object[]> getAllEmployeeState() {
        return ResponseEntity.ok(userRepo.getAllUserStats());
    }


}
