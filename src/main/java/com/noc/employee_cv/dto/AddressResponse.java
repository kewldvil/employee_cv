package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private String provinceNameKh;
    private String districtNameKh;
    private String communeNameKh;
    private String villageNameKh;
    private String streetNumber;
    private String houseNumber;
}
