package com.noc.employee_cv.provinces;

import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String commune_code;
    private String commune_name_kh;
    private String commune_name_en;
//
//    @OneToMany(mappedBy = "commune", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    private Set<Village> village;
//
//    @ManyToOne
//    @JoinColumn(name = "district_id")
//    private District district;

}
