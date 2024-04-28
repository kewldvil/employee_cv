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

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    public Integer id;
    private String streetNumber;
    private String houseNumber;
    @NotNull
    private int communeId;
    @NotNull
    private int districtId;
    @NotNull
    private int villageId;
    @NotNull
    private int provinceCityId;
    private AddressType addressType;
}
