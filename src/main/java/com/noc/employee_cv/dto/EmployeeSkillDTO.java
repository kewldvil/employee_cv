package com.noc.employee_cv.dto;

import lombok.Data;

@Data
public class EmployeeSkillDTO {
    private Integer id;
    private String skillName;
    private boolean enabled;
}
