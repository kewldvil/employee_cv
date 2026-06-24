package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobHistoryResponse {
    private int id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String activityAndRank;
    private String departmentOrUnit;

}
