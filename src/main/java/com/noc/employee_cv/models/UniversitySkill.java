package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class UniversitySkill {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String skill;
}
