package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceCity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String  provinceCityCode;
    private String  provinceCityNameKh;
    private String  provinceCityNameEn;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
