package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ParentDTO {
    public String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public LocalDate dateOfBirth;
    public String job;
    public Boolean isAlive;
    public List<PhoneNumberDTO> phoneNumberList;
    public AddressDTO currentAddress;
    public AddressDTO placeOfBirth;
}
