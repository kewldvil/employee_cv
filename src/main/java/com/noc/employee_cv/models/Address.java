package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToOne
    @JoinColumn(name="province_city_id")
    private ProvinceCity provinceCity;
    @OneToOne
    @JoinColumn(name="district_id")
    private District district;
    @OneToOne
    @JoinColumn(name="commune_id")
    private Commune commune;
    @OneToOne
    @JoinColumn(name="village_id")
    private Village village;

    private String addressType;
}
