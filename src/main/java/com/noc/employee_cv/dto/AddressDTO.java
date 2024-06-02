package com.noc.employee_cv.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Data
public class AddressDTO {
    private int province;
    private int district;
    private int commune;
    private int village;
    private String streetNumber;
    private String houseNumber;
}
