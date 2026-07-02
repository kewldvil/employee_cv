package com.noc.employee_cv.controller;


import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.UserEmployeeDTO;
import com.noc.employee_cv.enums.PoliceRank;
import com.noc.employee_cv.model.*;
import com.noc.employee_cv.services.serviceImpl.EmployeeServiceImpl;
import com.noc.employee_cv.services.serviceImpl.FileServiceImpl;
import com.noc.employee_cv.services.serviceImpl.ReportServiceImpl;
import com.noc.employee_cv.services.serviceImpl.UserServiceImpl;
import jakarta.mail.MessagingException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EmployeeController {
    private final EmployeeServiceImpl service;
    private final ReportServiceImpl reportService;
    private final UserServiceImpl userService;
    private final FileServiceImpl fileService;
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> createNewEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        service.save(req);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid EmployeeDTO req) throws MessagingException {
        service.update(req);
        return ResponseEntity.accepted().build();
    }

    //    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) throws MessagingException {
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
    public ResponseEntity<EmployeeDTO> getEmployeeByUserId(@PathVariable Integer id) throws MessagingException {
        EmployeeDTO employee = service.findDetailDtoByUserId(id, fileService.getFileNamesByUserId(id));
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/{userId}/{employeeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<EmployeeDTO> getEmployeeByUserIdAndEmployeeId(@PathVariable Integer userId, @PathVariable Integer employeeId) throws MessagingException {
        EmployeeDTO employee = service.findDetailDtoByUserIdAndEmployeeId(employeeId, userId, fileService.getFileNamesByUserId(userId));
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
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

        // Validate page and size parameters early
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page and size must be positive.");
        }

        // Efficient pagination handling
        Pageable pageable = PageRequest.of(page, size);

        // Fetch data based on filter
        Page<User> users = switch (filter) {
            case "employee" -> (departmentId == 0)
                    ? userService.findAllActiveUser(search, pageable)
                    : userService.findByDepartmentId(departmentId, search, pageable);
            case "weapon" -> userService.findAllUsersWithEmployeeAndWeaponsByDepartment(departmentId, search, pageable);
            case "car" -> userService.findAllUsersWithEmployeeAndPoliceCarByDepartment(departmentId, search, pageable);
            default -> throw new IllegalArgumentException("Invalid filter value.");
        };

        // If no content, return HTTP 404 Not Found
        if (!users.hasContent()) {
            return ResponseEntity.notFound().build();
        }

        // Map users to DTOs
        List<UserEmployeeDTO> employees = users.stream()
                .map(EmployeeController::convertToDTO)
                .collect(Collectors.toList());

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
        userEmployeeDTO.setRole(user.getRoleName());
        if (user.getEmployee() != null) {
            userEmployeeDTO.setGender(user.getEmployee().getGender());
            userEmployeeDTO.setIsMarried(user.getEmployee().getIsMarried());
            userEmployeeDTO.setCurrentPosition(user.getEmployee().getCurrentPosition().getPosition());
            userEmployeeDTO.setCurrentPositionSortOrder(user.getEmployee().getCurrentPosition().getSortOrder());
            userEmployeeDTO.setCurrentPoliceRank(user.getEmployee().getCurrentPoliceRank());
            userEmployeeDTO.setCurrentPoliceRankSortOrder(PoliceRank.getKeyByValue(userEmployeeDTO.getCurrentPoliceRank()));
//            userEmployeeDTO.setDegreeLevels(user.getEmployee().getEmployeeDegreeLevels());
            userEmployeeDTO.setDepartmentName(user.getEmployee().getDepartment().getName());
            userEmployeeDTO.setDepartmentId(user.getEmployee().getDepartment().getId());
        }

        return userEmployeeDTO;
    }
}
