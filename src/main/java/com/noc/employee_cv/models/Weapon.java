package com.noc.employee_cv.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table
public class Weapon {
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "weapon")
    private Set<EmployeeWeapon> employeeWeapon;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String weaponName;

}
