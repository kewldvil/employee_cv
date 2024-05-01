package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.ProvinceCity;
import com.noc.employee_cv.provinces.Village;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeAddress> employeeAddresses = new HashSet<EmployeeAddress>();

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    public Integer id;
    private String streetNumber;
    private String houseNumber;
    @NotNull
    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_city_id")
    private ProvinceCity provinceCity;
    @NotNull
    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id")
    private District district;
    @NotNull
    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commune_id")
    private Commune commune;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "village_id")
    private Village village;
    }
