package com.noc.employee_cv.provinces;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceResponse {

    @Id
    private Integer id;
    private String province_city_code;
    private String province_city_name_kh;
    private String province_city_name_en;

//    private String district_code;
//    private String district_name_kh;
//    private String district_name_en;
//
//    private String commune_code;
//    private String commune_name_kh;
//    private String commune_name_en;
//
//    private String village_code;
//    private String village_name_kh;
//    private String village_name_en;
}
