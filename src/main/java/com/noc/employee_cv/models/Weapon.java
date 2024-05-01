package com.noc.employee_cv.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String weaponName;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
