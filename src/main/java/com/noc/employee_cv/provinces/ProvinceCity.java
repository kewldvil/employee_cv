package com.noc.employee_cv.provinces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.AddressProvinceCity;
import com.noc.employee_cv.models.Employee;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @OneToMany(mappedBy = "provinceCity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressProvinceCity> addressProvinceCities=new ArrayList<>();
}
