package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class GeneralDepartmentDTO {
    private Integer id;
    private String name;
    private boolean enabled;
    private Set<DepartmentDTO> departmentDTOs;
}
