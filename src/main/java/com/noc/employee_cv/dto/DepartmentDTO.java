package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentDTO {
    private Integer id;
    private String name;
    private boolean enabled;
    private Integer generalDepartmentId;

}
