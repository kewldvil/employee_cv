package com.noc.employee_cv.provinces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "province_city_id", nullable = false)
    private int province_city_id;
    @Column(name = "district_code", nullable = false)
    private String district_code;
    private String district_name_kh;
    private String district_name_en;
    private boolean enabled ;


    @JsonIgnore
    @ManyToMany(mappedBy = "districts")
    private List<Address> addresses = new ArrayList<>();
}
