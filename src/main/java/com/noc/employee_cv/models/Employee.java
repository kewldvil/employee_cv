package com.noc.employee_cv.models;


import com.noc.employee_cv.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String firstName;
    private String lastName;

    private Gender gender;
    private LocalDate dateOfBirth;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Address> employeeAddress;

    private PoliceRank currentPoliceRank;
    private Position currentPosition;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<PhoneNumber> phoneNumber;
    @Column(length = 5)
    private BloodType bloodType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<PolicePlateNumberCar> policePlatNumberCars;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<EmployeeWeapon> employeeWeapon;
    private EducationLevel educationLevel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<UniversitySkill> universitySkill;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<EmployeeLanguage> foreignLanguage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Appreciation> appreciation;
    private LocalDate dateJoinGovernmentJob;
    private LocalDate dateJoinPoliceJob;
    private PoliceRank previousPoliceRank;
    private Position previousPosition;
    private String policeDepartment;

    private LocalDate previousActivityAndPositionDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<PreviousActivityAndPosition> activityAndPosition;


    private boolean isMarried;
    @OneToOne
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @OneToOne
    @JoinColumn(name = "employee_parent_id")
    private EmployeeParent employeeParent;


}

