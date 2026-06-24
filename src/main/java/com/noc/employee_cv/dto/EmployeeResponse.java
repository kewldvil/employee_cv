package com.noc.employee_cv.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeResponse {

    private Integer id;

    // Personal Information
    private String firstname;
    private String lastname;
    private String latinName;
    private String policeId;
    private String gender;

    private LocalDate dateOfBirth;
    private AddressResponse placeOfBirth;
    private String policeRank;
    private String policeRankDocumentNumber;
    private LocalDate policeRankDocumentIssueDate;
    private String position;
    private String positionDocumentNumber;
    private LocalDate positionDocumentIssueDate;
    private AddressResponse currentAddress;
    private List<String> phoneNumbers;
    private String bloodType;
    private List<PolicePlateNumberCarResponse> policePlateNumberCars;
    private List<WeaponResponse> weapons;
    // EDUCATION LEVEL
    private List<EmployeeDegreeLevelResponse> degreeLevels;
    private List<String> majors;
    // FOREIGN LANGUAGES
    private List<String> languages;
    // TRAINING
    private List<VocationalTrainingResponse> vocationalTrainings;
    //    APPRECIATION
    private List<AppreciationResponse> appreciations;
    //    JOB HISTORY
    private LocalDate dateJoinGov;
    private LocalDate dateJoinPolice;
    private String prevPoliceRank;
    private String prevPosition;
    private String generalDepartment;
    private String previousActivityAndPositionStartYear;
    private List<JobHistoryResponse> jobHistories;
    //    FAMILY
    private SpouseResponse spouse;
    private ParentResponse father;
    private ParentResponse mother;

}
