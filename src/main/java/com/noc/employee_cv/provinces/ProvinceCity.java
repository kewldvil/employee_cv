package com.noc.employee_cv.provinces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "province_city")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProvinceCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String province_city_code;
    private String province_city_name_kh;
    private String province_city_name_en;

    @JsonIgnore
    @ManyToMany(mappedBy = "provinces")
    private List<Address> addresses = new ArrayList<>();
}
