package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String  CommuneCode;
    private String  CommuneNameKh;
    private String  CommuneNameEn;
    @OneToOne(mappedBy = "commune")
    private Address address;
}
