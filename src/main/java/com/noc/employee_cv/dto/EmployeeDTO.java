package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.noc.employee_cv.enums.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class EmployeeDTO {
    private Integer id;
    private Integer userId;
    private String firstname;
    private String lastname;
    private String latinName;
    private String policeId;
    private String nationality;
    private Boolean isMarried;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String gender;
    private AddressDTO placeOfBirth;
    private String currentPoliceRank;
    private String policeRankDocumentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate policeRankDocumentIssueDate;
    private String currentPosition;
    private String positionDocumentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate positionDocumentIssueDate;
    private AddressDTO currentAddress;
    private String bloodType;
    private List<PhoneNumberDTO> phoneNumberList;
    private List<WeaponDTO> weaponList;
    private List<PoliceCarDTO> policeCarList;
    private List<educationDTO> educationList;
    private List<UniversityMajorDTO> UniversityMajorList;
    private List<ForeignLanguageDTO> foreignLangList;
    private List<VocationalTrainingDTO> vocationalTrainingList;
    private List<AppreciationDTO> appreciationList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinGov;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinPolice;
    private String prevPoliceRank;
    private String prevPosition;
    private String generalDepartment;
    private int previousActivityAndPositionStartYear;
    private List<PreviousActivityAndPositionDTO> activityAndPosition;
    private ParentDTO father;
    private ParentDTO mother;
    private SpouseDTO spouse;

}
