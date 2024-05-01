package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class SpouseDTO{
    public String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public LocalDate dateOfBirth;
    public String job;
    public Boolean isAlive;
    public List<PhoneNumberDTO> phoneNumberList;
    public AddressDTO currentAddress;
    public AddressDTO placeOfBirth;

    private List<ChildDTO> childrenList;
}
