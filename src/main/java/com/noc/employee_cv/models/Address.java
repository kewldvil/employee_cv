package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.ProvinceCity;
import com.noc.employee_cv.provinces.Village;
import jakarta.persistence.*;
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
//    private int communeId;
//    private int districtId;
//    private int villageId;
//    private int provinceCityId;
//    @OneToOne
//    @JoinColumn(name="village_id")
//    private Village village;

    private AddressType addressType;
}
