package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table
@Data
public class Address {
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    public Integer id;
//    private int communeId;
//    private int districtId;
//    private int villageId;
//    private int provinceCityId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="province_city_id")
    private ProvinceCity provinceCity;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="district_id")
    private District district;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="commune_id")
    private Commune commune;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="village_id")
    private Village village;

    private String addressType;
}
