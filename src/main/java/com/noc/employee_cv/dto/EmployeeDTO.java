package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noc.employee_cv.dto.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeDTO {

    private Integer id;

    // Personal Information
    private String firstname;
    private String lastname;
    private String latinName;
    private String nationality;
    private String gender;
    private String policeId;
    private Boolean isMarried;
    private String bloodType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String phoneNumber;

    // Police and Position Details
    private String currentPoliceRank;
    private int currentPositionId;
    private String policeRankDocumentNumber;
    private String positionDocumentNumber;
    private int departmentId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate policeRankDocumentIssueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate positionDocumentIssueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinGov;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinPolice;

    private String prevPoliceRank;
    private int previousPositionId;
    private String generalDepartment;
    private int previousActivityAndPositionStartYear;

    // Address Information
    private AddressDTO placeOfBirth;
    private AddressDTO currentAddress;

    // Relationships with other entities
    private SpouseDTO spouse;
    private ParentDTO father;
    private ParentDTO mother;
    private Integer userId;

    private List<PolicePlateNumberCarDTO> policePlateNumberCars;
    private List<WeaponDTO> weapons;
    private List<EmployeeDegreeLevelDTO> degreeLevels;
    private List<AppreciationDTO> appreciations;
    private List<VocationalTrainingDTO> vocationalTrainings;
    private List<PreviousActivityAndPositionDTO> activityAndPositions;
    private List<EmployeeLanguageDTO> employeeLanguages;
    private List<EmployeeSkillDTO> employeeSkills;
}
