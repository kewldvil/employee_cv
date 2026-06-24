package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildResponse {
    private int id;
    private String childName;
    private String childGender;
    private String childDateOfBirth;
    private String childJob;
}
