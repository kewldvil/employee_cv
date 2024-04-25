package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table
public class Spouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String spouseFirstName;
    private String spouseLastName;
    private boolean isAlive;
    private LocalDate spouseDateOfBirth;
    private String spouseJobName;
    private String spousePhoneNumber;
    private int childrenNumber;
    private int numberOfDaughters;
    private int numberOfSons;
    @OneToMany(mappedBy = "spouse")
    private Set<SpouseChildren> children;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
