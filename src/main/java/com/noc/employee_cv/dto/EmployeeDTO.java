package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.noc.employee_cv.enums.Gender;

import com.noc.employee_cv.enums.UniversityMajor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class EmployeeDTO {
    private String firstname;
    private String lastname;
    private Boolean isMarried=true;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private Gender gender;
    private AddressDTO placeOfBirth;
    private int currentPoliceRankId;
    private String policeRankDocumentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate policeRankDocumentIssueDate;
    private int currentPositionId;
    private String positionDocumentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate positionDocumentIssueDate;
    private AddressDTO currentAddress;
    private int bloodTypeId;
    private List<PhoneNumberDTO> phoneNumberList;
    private List<WeaponDTO> weaponList;
    private List<PoliceCarDTO> policeCarList;
    private List<Boolean> educationList;
    private List<UniversityMajorDTO> UniversityMajorList;
    private List<ForeignLanugeDTO> foreignLangList;
    private List<VocationalTrainingDTO> vocationalTrainingList;
    private List<AppreciationDTO> appreciationList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinGov;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinPolice;
    private int prevPoliceRankId;
    private int prevPositionId;
    private int departmentId;
    private int previousActivityAndPositionStartYear;
    private List<PreviousActivityAndPositionDTO> activityList;
    private ParentDTO father;
    private ParentDTO mother;
    private SpouseDTO spouse;


}
