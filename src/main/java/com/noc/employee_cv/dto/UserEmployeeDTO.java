package com.noc.employee_cv.dto;

import com.noc.employee_cv.models.EmployeeDegreeLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserEmployeeDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String gender;
    private Boolean isMarried;
    private String CurrentPoliceRank;
    private String CurrentPosition;
    private Set<EmployeeDegreeLevel> degreeLevels;
    private String ImagePath;
    public String ImageName;
    public String getFullName() {
        return lastname + " " + firstname;
    }
}
