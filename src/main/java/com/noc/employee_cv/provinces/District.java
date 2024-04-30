package com.noc.employee_cv.provinces;

import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Entity
@Data
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String district_code;
    private String district_name_kh;
    private String district_name_en;
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Commune> commune;

    @ManyToOne
    @JoinColumn(name = "province_city_id")
    private ProvinceCity provinceCity;
}
