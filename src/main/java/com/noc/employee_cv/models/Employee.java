package com.noc.employee_cv.models;


import com.noc.employee_cv.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;

    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate dateOfBirth;
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<Address> employeeAddress;

    @NotNull
    private PoliceRank currentPoliceRank;
    @NotNull
    private Position currentPosition;

    @OneToMany(mappedBy = "employee")
    private Set<PhoneNumber> phoneNumber;
    @Column(length = 5)
    private BloodType bloodType;
    @OneToMany(mappedBy = "employee")
    private Set<PolicePlateNumberCar> policePlatNumberCars;
    @OneToMany(mappedBy = "employee")
    private Set<EmployeeWeapon> employeeWeapon;
    private EducationLevel educationLevel;
    @OneToMany(mappedBy = "employee")
    private Set<UniversitySkill> universitySkill;
    @OneToMany(mappedBy = "employee")
    private Set<EmployeeLanguage> foreignLanguage;

    @OneToMany(mappedBy = "employee")
    private Set<Appreciation> appreciation;
    @NotNull
    private LocalDate dateJoinGovernmentJob;
    @NotNull
    private LocalDate dateJoinPoliceJob;
    private PoliceRank previousPoliceRank;
    @NotNull
    private Position previousPosition;
    @NotNull
    private String policeDepartment;

    private LocalDate previousActivityAndPositionDate;
    @OneToMany(mappedBy = "employee")
    private Set<PreviousActivityAndPosition> activityAndPosition;

    @NotNull
    private boolean isMarried;
    @OneToOne
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @OneToOne
    @JoinColumn(name = "employee_parent_id")
    private EmployeeParent employeeParent;


}

