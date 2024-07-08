package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class PrevActivityPDFDTO {
    private Integer id;
    private String fromDate;
    private String toDate;
    private String activityAndRank;
    private String departmentOrUnit;
    private Boolean isNoStartDayMonth;
    private Boolean isNoEndDayMonth;
}
