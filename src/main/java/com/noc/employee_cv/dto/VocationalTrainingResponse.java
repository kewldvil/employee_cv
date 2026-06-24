package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VocationalTrainingResponse {
    private int id;
    private LocalDate trainingStartDate;
    private LocalDate trainingEndDate;
    private String trainingDuration;
    private String trainingCourse;
    private String trainingCenter;
}
