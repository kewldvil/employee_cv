package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commune {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String  CommuneCode;
    private String  CommuneNameKh;
    private String  CommuneNameEn;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
