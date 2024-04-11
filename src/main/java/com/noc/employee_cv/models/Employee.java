package com.noc.employee_cv.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String firstName;
    private String lastName;

    private String gender;
    private LocalDate dateOfBirth;

    //    private Address placeOfBirth;
    private int currentPoliceRankID;
    private int currentPositionID;
    //    private List<Address> addressList;
    private String phoneNumber;
    private int bloodTypeID;
    //    private List<PolicePlatNumberCar> policePlatNumberCars;
//    private List<Weapon> weapon;
    private int educationLevelID;
//    private Map<UniversitySkill> universitySkillList;
//    private List<ForeignLanguage> foreignLanguageList;

    //    private List<Appreciation> appreciationList;
    private LocalDate dateJoinGovernmentJob;
    private LocalDate dateJoinPoliceJob;
    private int previousPoliceRankID;
    private int previousPositionID;

    private LocalDate previousActivityAndPositionDate;
//    private List<PreviousActivityAndPosition> activityAndPositionList;


    private boolean isMarried;
    private int spouseId;
//    private List<SpouseChildren> spouseChildrenList;
//    private List<EmployeeParent> parentList;
}

