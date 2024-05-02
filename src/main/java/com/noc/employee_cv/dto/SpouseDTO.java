package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class SpouseDTO{
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String job;
    private boolean isAlive=false;
    private List<PhoneNumberDTO> phoneNumberList;
    private AddressDTO currentAddress;
    private AddressDTO placeOfBirth;

    private List<ChildDTO> childrenList;
}
