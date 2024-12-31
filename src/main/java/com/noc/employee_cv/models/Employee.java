package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.dto.UserFileDTO;
import com.noc.employee_cv.provinces.ProvinceCity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Personal Information
    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String latinName;

    @NotNull
    private String nationality;

    private String gender;

    @NotNull
    private String policeId;

    @NotNull
    private Boolean isMarried = true;

    private String bloodType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate dateOfBirth;

    private String phoneNumber;

    // Police and Position Details
    private String currentPoliceRank;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate dateJoinGov;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate dateJoinPolice;

    private String prevPoliceRank;
    private String generalDepartment;
    private int previousActivityAndPositionStartYear;
    @Transient
    private List<UserFileDTO> fileNames;
    // Police and Position Details
    @ManyToOne
    @JoinColumn(name = "current_position_id")
    private Position currentPosition;

    @ManyToOne
    @JoinColumn(name = "previous_position_id")
    private Position previousPosition;
    // Address Information
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "place_of_birth_id", referencedColumnName = "id")
    private Address placeOfBirth;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_address_id", referencedColumnName = "id")
    private Address currentAddress;

    // Relationships with other entities
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Spouse spouse;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Father father;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Mother mother;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bureau_id")
    private Bureau bureau;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PolicePlateNumberCar> policePlateNumberCars = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Weapon> weapons = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeDegreeLevel> employeeDegreeLevels = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appreciation> appreciations = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VocationalTraining> vocationalTrainings = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreviousActivityAndPosition> activityAndPositions = new ArrayList<>();


    @OneToMany(mappedBy = "employee")
    private Set<EmployeeLanguage> employeeLanguages = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "employee_skill",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    // Timestamps
    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    public String getFormattedDateOfBirth() {
        if (dateOfBirth != null) {
            return dateOfBirth.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        return "";
    }
}
