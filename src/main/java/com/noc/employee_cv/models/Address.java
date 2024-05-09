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
    @JsonIgnore
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeAddress> employeeAddresses = new HashSet<>();

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Integer id;
    private String streetNumber;
    private String houseNumber;
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressProvinceCity> addressProvinceCities = new ArrayList<>();
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressDistrict> addressDistricts= new ArrayList<>();
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressCommune>addressCommunes= new ArrayList<>();
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressVillage> addressVillages= new ArrayList<>();
    }
