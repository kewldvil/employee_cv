package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "employee_weapon")
public class EmployeeWeapon {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "weapon_id")
    private Weapon weapon;


    private String weaponType;
}
