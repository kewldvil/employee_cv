package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeDTO {

    private Integer id;

    // Personal Information
    @NotBlank(message = "firstname is required")
    @Size(max = 100, message = "firstname must be 100 characters or less")
    private String firstname;

    @NotBlank(message = "lastname is required")
    @Size(max = 100, message = "lastname must be 100 characters or less")
    private String lastname;

    @NotBlank(message = "latinName is required")
    @Size(max = 150, message = "latinName must be 150 characters or less")
    private String latinName;

    @NotBlank(message = "nationality is required")
    @Size(max = 100, message = "nationality must be 100 characters or less")
    private String nationality;

    @NotBlank(message = "gender is required")
    private String gender;

    @NotBlank(message = "policeId is required")
    @Size(max = 50, message = "policeId must be 50 characters or less")
    private String policeId;

    @NotNull(message = "isMarried is required")
    private Boolean isMarried;

    private String bloodType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;

    @Size(max = 30, message = "phoneNumber must be 30 characters or less")
    private String phoneNumber;

    // Police and Position Details
    @NotBlank(message = "currentPoliceRank is required")
    private String currentPoliceRank;

    @Positive(message = "currentPositionId is required")
    private int currentPositionId;

//    @NotBlank(message = "policeRankDocumentNumber is required")
    private String policeRankDocumentNumber;

//    @NotBlank(message = "positionDocumentNumber is required")
    private String positionDocumentNumber;

    @Positive(message = "departmentId is required")
    private int departmentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "policeRankDocumentIssueDate is required")
    private LocalDate policeRankDocumentIssueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "positionDocumentIssueDate is required")
    private LocalDate positionDocumentIssueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
//    @NotNull(message = "dateJoinGov is required")
    private LocalDate dateJoinGov;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "dateJoinPolice is required")
    private LocalDate dateJoinPolice;

    private String prevPoliceRank;

    @Positive(message = "previousPositionId is required")
    private int previousPositionId;

    private String generalDepartment;

    private int previousActivityAndPositionStartYear;

    // Address Information
    @Valid
    private AddressDTO placeOfBirth;

    @Valid
    private AddressDTO currentAddress;

    // Relationships with other entities
    @Valid
    private SpouseDTO spouse;
    @Valid
    private ParentDTO father;
    @Valid
    private ParentDTO mother;

    @NotNull(message = "userId is required")
    private Integer userId;

    @Valid
    private List<PolicePlateNumberCarDTO> policePlateNumberCars;
    @Valid
    private List<WeaponDTO> weapons;
    @Valid
    private List<EmployeeDegreeLevelDTO> degreeLevels;
    @Valid
    private List<AppreciationDTO> appreciations;
    @Valid
    private List<VocationalTrainingDTO> vocationalTrainings;
    @Valid
    private List<PreviousActivityAndPositionDTO> activityAndPositions;
    @Valid
    private List<EmployeeLanguageDTO> employeeLanguages;
    @Valid
    private List<EmployeeSkillDTO> employeeSkills;
}
