package com.noc.employee_cv.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.noc.employee_cv.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate dateOfBirth;
    @OneToMany(mappedBy = "employee")
    private Set<EmployeeAddress> employeeAddresses = new HashSet<EmployeeAddress>();

    @NotNull
    private PoliceRank currentPoliceRank;
    @NotNull
    private Position currentPosition;
    @NotNull
    private String policeRankDocumentNumber;
    @NotNull
    private String positionDocumentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate policeRankDocumentIssueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate positionDocumentIssueDate;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinGovernmentJob;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
    private boolean isMarried=true;
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_parent_id")
    private EmployeeParent employeeParent;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

