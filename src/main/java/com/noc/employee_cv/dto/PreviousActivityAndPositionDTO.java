package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PreviousActivityAndPositionDTO {
    private String fromDateToDate;
    private String activityAndRank;
    private String departmentOrUnit;
}
