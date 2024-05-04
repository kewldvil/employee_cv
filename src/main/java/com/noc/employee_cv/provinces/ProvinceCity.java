package com.noc.employee_cv.provinces;

import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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

}
