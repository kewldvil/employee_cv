package com.noc.employee_cv.provinces;

import com.noc.employee_cv.models.Address;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

@Entity
@Data
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String village_code;
    private String village_name_kh;
    private String village_name_en;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "commune_id")
    private Commune commune;


}
