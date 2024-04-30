package com.noc.employee_cv.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDTO {
    private String addressType;
    private int province;
    private int district;
    private int commune;
    private int village;
    private String streetNumber;
    private String houseNumber;
}
