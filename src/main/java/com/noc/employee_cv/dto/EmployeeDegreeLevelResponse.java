package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDegreeLevelResponse {
    private Integer id;
    private String degreeLevelNameKh;
    private Boolean isChecked;
}
