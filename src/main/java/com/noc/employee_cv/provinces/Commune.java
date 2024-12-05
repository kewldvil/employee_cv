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
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int district_id;
    private String commune_code;
    private String commune_name_kh;
    private String commune_name_en;
    private boolean enabled;
    @JsonIgnore
    @ManyToMany(mappedBy = "communes")
    private List<Address> addresses = new ArrayList<>();
}
