package com.noc.employee_cv.provinces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.AddressCommune;
import com.noc.employee_cv.models.AddressVillage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int district_id;
    private String commune_code;
    private String commune_name_kh;
    private String commune_name_en;

    @JsonIgnore
    @OneToMany(mappedBy = "commune", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressCommune> addressCommunes=new ArrayList<>();
}
