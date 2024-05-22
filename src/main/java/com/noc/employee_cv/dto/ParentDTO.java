package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class ParentDTO {
    private Integer id;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String job;
    private Boolean isAlive;
    private Set<PhoneNumberDTO> phoneNumberList;
    private AddressDTO currentAddress;
    private AddressDTO placeOfBirth;
}
