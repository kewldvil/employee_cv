package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Village {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String villageCode;
    private String villageNameKh;
    private String villageNameEn;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

}
