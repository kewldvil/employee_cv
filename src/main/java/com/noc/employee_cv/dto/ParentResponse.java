package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParentResponse {
    private int id;
    private String fullName;
    private Boolean isAlive;
    private LocalDate dateOfBirth;
    private String job;
    private AddressResponse placeOfBirth;
    private AddressResponse currentAddress;
}
