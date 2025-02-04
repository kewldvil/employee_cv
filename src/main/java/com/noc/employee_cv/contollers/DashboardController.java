package com.noc.employee_cv.contollers;

import com.noc.employee_cv.authentication.AuthenticationService;
import com.noc.employee_cv.authentication.IncorrectPasswordException;
import com.noc.employee_cv.dto.PoliceRankCountProjection;
import com.noc.employee_cv.dto.UserEmployeeDTO;
import com.noc.employee_cv.dto.UserProfileDTO;
import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.models.Department;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.Skill;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.DepartmentRepo;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImpl.EmployeeServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/managements")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final EmployeeServiceImp employeeService;
    private final UserRepo userRepo;
    private final AuthenticationService service;
    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;
    @Value("${file.signature-dir}")
    private String SIGNATURE_UPLOAD_DIR;

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
        System.out.println("GET USERS BY " + filter);
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


    @PutMapping(value = "/user/profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserProfile(
            @PathVariable Integer userId,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("role") String role,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // Update user profile logic
            User user = userRepo.findUserById(userId);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setEmail(email);
            user.setUsername(username);
            user.setRole(Role.valueOf(role));
            // Handle file upload (if present)
            if (file != null && !file.isEmpty()) {
                try {
                    // Define the upload directory
                    File dir = new File(SIGNATURE_UPLOAD_DIR);

                    // Create the upload directory if it doesn't exist
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // Construct the new filename using the username
                    String fileExtension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1]; // Get the file extension
                    String newFileName = user.getUsername() + "." + fileExtension; // Use username as the filename
                    String filePath = SIGNATURE_UPLOAD_DIR + newFileName; // Full file path

                    // Save the file to the target location
                    Path targetLocation = Paths.get(filePath).toAbsolutePath().normalize();
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("File saved: " + targetLocation);
                } catch (IOException e) {
                    // Handle file operation errors
                    System.err.println("Failed to save file: " + e.getMessage());
                    throw new RuntimeException("Failed to save file", e);
                }
            } else {
                try {
                    // Define possible file extensions
                    String[] possibleExtensions = {"jpg", "png"};

                    // Try to delete the file with each extension
                    boolean fileDeleted = false;
                    for (String extension : possibleExtensions) {
                        String filePath = SIGNATURE_UPLOAD_DIR + user.getUsername() + "." + extension;
                        Path fileToDelete = Paths.get(filePath).toAbsolutePath().normalize();

                        if (Files.deleteIfExists(fileToDelete)) {
                            System.out.println("File deleted: " + fileToDelete);
                            fileDeleted = true;
                            break; // Exit loop if file is deleted
                        }
                    }

                    if (!fileDeleted) {
                        System.out.println("No file found for user: " + user.getUsername());
                    }
                } catch (IOException e) {
                    // Handle file deletion errors
                    System.err.println("Failed to delete file: " + e.getMessage());
                    throw new RuntimeException("Failed to delete file", e);
                }
            }

            // Save the updated user
            userRepo.save(user);

            return ResponseEntity.accepted().body("User profile updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating user profile");
        }
    }

    @PutMapping("/user/department/{userId}/{departmentId}")
    public ResponseEntity<String> changeUserDepartment(@PathVariable Integer userId, @PathVariable Integer departmentId) {
        System.out.println(userId);
        System.out.println("change department");
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
//            System.out.println(employee.getUser());
//            System.out.println(employee.getDepartment().getName());


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
