package com.noc.employee_cv.dto;

import lombok.Data;

@Data
public class VTTrainingPDFDTO {
    private Integer id;
    private String trainingCenter;
    private String trainingCourse;
    private String trainingDuration;
    private String trainingStartDate;
    private String trainingToDate;
    private Boolean isNoStartDayMonth;
    private Boolean isNoEndDayMonth;
}
