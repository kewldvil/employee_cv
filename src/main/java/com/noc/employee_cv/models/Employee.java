package com.noc.employee_cv.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String firstName;
    private String lastName;

    private int genderId;
    private LocalDate dateOfBirth;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Address> employeeAddress;

    private int currentPoliceRankId;
    private int currentPositionId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<PhoneNumber> phoneNumber;
    private int bloodTypeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<PolicePlateNumberCar> policePlatNumberCars;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<EmployeeWeapon> employeeWeapon;
    private int educationLevelId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<UniversitySkill> universitySkill;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<EmployeeLanguage> foreignLanguage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Appreciation> appreciation;
    private LocalDate dateJoinGovernmentJob;
    private LocalDate dateJoinPoliceJob;
    private int previousPoliceRankId;
    private int previousPositionId;
    private String policeDepartment;

    private LocalDate previousActivityAndPositionDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<PreviousActivityAndPosition> activityAndPosition;


    private boolean isMarried;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_parent_id")
    private EmployeeParent employeeParent;


}

