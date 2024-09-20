package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class VocationalTrainingDTO {
    private Integer id;
    private String trainingCenter;
    private String trainingCourse;
    private String trainingDuration;
    private Boolean isNoStartDayMonth;
    private Boolean isNoEndDayMonth;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate trainingStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate trainingToDate;
}
