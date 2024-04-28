package com.noc.employee_cv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String spouseFullName;
    @NotNull
    private boolean isAlive;
    @NotNull
    private String spouseGender;
    @NotNull
    private LocalDate spouseDateOfBirth;
    @NotNull
    private String spouseJobName;
    private String spousePhoneNumber;
    private int childrenNumber;
    private int numberOfDaughters;
    @OneToMany(mappedBy = "spouse")
    private Set<SpouseChildren> children;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
