package com.noc.employee_cv.dto;

import com.noc.employee_cv.models.Department;
import com.noc.employee_cv.models.EmployeeDegreeLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserEmployeeDTO {

    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String gender;
    private Boolean isMarried;
    private Boolean enabled;
    private String CurrentPoliceRank;
    private int CurrentPoliceRankSortOrder;
    private String CurrentPosition;
    private int currentPositionSortOrder;
    private int departmentId;
    private String departmentName;
    private Set<EmployeeDegreeLevel> degreeLevels;
    private String ImagePath;
    public String ImageName;
    public String getFullName() {
        return lastname + " " + firstname;
    }
}
