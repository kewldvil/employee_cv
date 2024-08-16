package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class AppreciationPDFDTO {
    private Integer id;
    private String appreciationNumber;
    private String appreciationDate;
    private String appreciation;
    private LocalDate realAppreciationDate;
}
