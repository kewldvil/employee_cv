package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ParentDTO {
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String job;
    private boolean isAlive;
    private List<String> phoneNumbers;
    private AddressDTO currentAddress;
    private AddressDTO placeOfBirth;
}
