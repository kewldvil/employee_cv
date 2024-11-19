package com.noc.employee_cv.contollers;


import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.UserEmployeeDTO;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.services.serviceImpl.EmployeeServiceImp;
import com.noc.employee_cv.services.serviceImpl.ReportServiceImp;
import com.noc.employee_cv.services.serviceImpl.UserServiceImpl;
import jakarta.mail.MessagingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImp service;
    private final UserRepo userRepo;
    private final ReportServiceImp reportService;
    private final UserServiceImpl userService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> createNewEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        service.save(req);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        System.out.println("update employee");
        service.update(req);
        return ResponseEntity.accepted().build();
    }

    //    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) throws MessagingException {
//        System.out.println("GET EMPLOYEE BY ID");
//        Employee employee = service.findById(id);
//        if (employee != null) {
//            // If response body is not null, return it with HTTP status 200 OK
//            return ResponseEntity.ok(employee);
//        } else {
//            // If response body is null, return 404 Not Found status code
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) throws MessagingException {
        System.out.println("GET EMPLOYEE BY ID");
        Employee employee = service.findById(id);

        if (employee != null) {
            // Return the employee object with HTTP status 200 OK
            return ResponseEntity.ok(employee);
        } else {
            // If employee not found, return 404 Not Found status
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeByUserId(@PathVariable Integer id) throws MessagingException {
        System.out.println("GET EMPLOYEE BY USER ID");
        Employee employee = service.findByUserId(id);
        if (employee != null) {
            // Check if the spouse and children are not null
            if (employee.getSpouse() != null && employee.getSpouse().getChildren() != null) {
                // Convert Set to List for sorting
                List<SpouseChildren> childrenList = new ArrayList<>(employee.getSpouse().getChildren());
                System.out.println("Sorted Children details by Date of Birth (DESC):");
                for (SpouseChildren child : childrenList) {
                    System.out.println("Full Name: " + child.getFullName());
                    System.out.println("Date of Birth: " + child.getDateOfBirth());
                    System.out.println("Gender: " + child.getGender());
                    System.out.println("Job: " + child.getJob());
                }
                // Sort the list by date of birth in descending order, handling nulls
                childrenList.sort(Comparator.nullsLast(Comparator.comparing(SpouseChildren::getDateOfBirth, Comparator.nullsLast(Comparator.naturalOrder()))));

                // Optionally, if you need to store the sorted list back into a Set
                employee.getSpouse().setChildren(new LinkedHashSet<>(childrenList));

                // Print sorted children details
            } else {
                System.out.println("No spouse or children found for the employee.");
            }

            // Sort vocational trainings by training start date ascending, handling nulls
            if (employee.getVocationalTrainings() != null) {
                employee.getVocationalTrainings().sort(Comparator.nullsLast(Comparator.comparing(VocationalTraining::getTrainingStartDate, Comparator.nullsLast(Comparator.naturalOrder()))));
            }

            // Sort appreciations by appreciation date ascending, handling nulls
            if (employee.getAppreciations() != null) {
                employee.getAppreciations().sort(Comparator.nullsLast(Comparator.comparing(Appreciation::getAppreciationDate, Comparator.nullsLast(Comparator.naturalOrder()))));
            }

            // Sort job history by job start date ascending, handling nulls
            if (employee.getActivityAndPositions() != null) {
                employee.getActivityAndPositions().sort(Comparator.nullsLast(Comparator.comparing(PreviousActivityAndPosition::getFromDate, Comparator.nullsLast(Comparator.naturalOrder()))));
            }

            // If response body is not null, return it with HTTP status 200 OK
            return ResponseEntity.ok(employee);
        } else {
            // If response body is null, return 404 Not Found status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/{userId}/{employeeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> getEmployeeByUserIdAndEmployeeId(@PathVariable Integer userId, @PathVariable Integer employeeId) throws MessagingException {
        System.out.println("getEmployeeByUserIdAndEmployeeId");
        Employee employee = service.findByUserIdAndEmployeeId(employeeId, userId);

        if (employee != null) {
            // If response body is not null, return it with HTTP status 200 OK
            return ResponseEntity.ok(employee);
        } else {
            // If response body is null, return 404 Not Found status code
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam String filter,
            @RequestParam Integer departmentId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page and size must be positive.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;

        // Handle filter conditions
        switch (filter) {
            case "employee" -> users = (departmentId == 0)
                    ? userService.findAllActiveUser(search, pageable)
                    : userService.findByDepartmentId(departmentId, search, pageable);
            case "weapon" -> users = userService.findAllUsersWithEmployeeAndWeaponsByDepartment(departmentId, search, pageable);
            case "car" -> users = userService.findAllUsersWithEmployeeAndPoliceCarByDepartment(departmentId, search, pageable);
            default -> throw new IllegalArgumentException("Invalid filter value.");
        }

        // Check if users list is empty
        if (!users.hasContent()) {
            return ResponseEntity.notFound().build();
        }

        // Map users to DTOs
        List<UserEmployeeDTO> employees = users.stream()
                .map(EmployeeController::convertToDTO)
                .toList();

        // Prepare response with pagination details
        Map<String, Object> response = new HashMap<>();
        response.put("employees", employees);
        response.put("currentPage", users.getNumber());
        response.put("totalItems", users.getTotalElements());
        response.put("totalPages", users.getTotalPages());

        return ResponseEntity.ok(response);
    }



    @GetMapping("/report/{format}/{empId}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format, @PathVariable Integer empId) throws JRException, IOException {
        return reportService.exportReportToFrontEnd(format, empId);
    }


    public static UserEmployeeDTO convertToDTO(User user) {
        UserEmployeeDTO userEmployeeDTO = new UserEmployeeDTO();
        userEmployeeDTO.setId(user.getId());
        userEmployeeDTO.setUsername(user.getUsername());
        userEmployeeDTO.setImageName(user.getImageName());
        userEmployeeDTO.setImagePath(user.getImagePath());
        userEmployeeDTO.setFirstname(user.getFirstname());
        userEmployeeDTO.setLastname(user.getLastname());
        userEmployeeDTO.setEmail(user.getEmail());
        userEmployeeDTO.getFullName();
        userEmployeeDTO.setEnabled(user.isEnabled());
        userEmployeeDTO.setRole(user.getRole().name());
        if (user.getEmployee() != null) {
            userEmployeeDTO.setGender(user.getEmployee().getGender());
            userEmployeeDTO.setIsMarried(user.getEmployee().getIsMarried());
            userEmployeeDTO.setCurrentPosition(user.getEmployee().getCurrentPosition());
            userEmployeeDTO.setCurrentPoliceRank(user.getEmployee().getCurrentPoliceRank());
//            userEmployeeDTO.setDegreeLevels(user.getEmployee().getEmployeeDegreeLevels());
            userEmployeeDTO.setDepartmentName(user.getEmployee().getDepartment().getName());
            userEmployeeDTO.setDepartmentId(user.getEmployee().getDepartment().getId());
        }

        return userEmployeeDTO;
    }
}