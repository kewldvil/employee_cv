package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ChildrenPDFDTO {
    private Integer id;
    private String fullName;
    private String gender;
    private String dateOfBirth;
    private String job;
}
