package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.ProvinceCity;
import com.noc.employee_cv.provinces.Village;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    private String streetNumber;
    private String houseNumber;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "address_provinces",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "province_id")
    )
    private List<ProvinceCity> provinces = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "address_districts",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "district_id")
    )
    private List<District> districts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "address_communes",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "commune_id")
    )
    private List<Commune> communes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "address_villages",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "village_id")
    )
    private List<Village> villages = new ArrayList<>();



}
