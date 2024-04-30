package com.noc.employee_cv.provinces;

import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Entity
@Data
@Table(name = "province_city")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceCity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String province_city_code;
    private String province_city_name_kh;
    private String province_city_name_en;

    @OneToMany(mappedBy = "provinceCity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<District> district;

}
