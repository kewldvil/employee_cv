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
@AllArgsConstructor
@NoArgsConstructor
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int commune_id;
    private String village_code;
    private String village_name_kh;
    private String village_name_en;
    private boolean enabled;

    @JsonIgnore
    @ManyToMany(mappedBy = "villages")
    private List<Address> addresses = new ArrayList<>();
}
