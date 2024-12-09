package com.noc.employee_cv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DepartmentDTO {
    private Integer id;
    private String name;
    private boolean enabled;
    private String generalDepartmentName;
    private Integer generalDepartmentId;

}
