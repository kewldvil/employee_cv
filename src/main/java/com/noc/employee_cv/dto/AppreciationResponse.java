package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppreciationResponse {
    private int id;
    private String appreciationNumber;
    private LocalDate appreciationDate;
    private String appreciation;
}
