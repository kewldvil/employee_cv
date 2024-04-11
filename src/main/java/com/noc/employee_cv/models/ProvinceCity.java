package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProvinceCity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String  provinceCityCode;
    private String  provinceCityNameKh;
    private String  provinceCityNameEn;
    @OneToOne(mappedBy = "provinceCity")
    private Address address;

}
