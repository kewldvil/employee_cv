package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String  DistrictCode;
    private String  DistrictNameKh;
    private String  DistrictNameEn;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
