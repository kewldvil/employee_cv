package com.noc.employee_cv.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class Officer extends Person{

    @NotNull
    private String nationality;
    @NotNull
    String policeId;
    @NotNull
    private Boolean isMarried = true;
    private String bloodType;
    @NotNull
    private String latinName;

    private String currentPoliceRank;

    private String currentPosition;
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
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinGov;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateJoinPolice;

    private String prevPoliceRank;

    private String prevPosition;

    private String generalDepartment;
    private int previousActivityAndPositionStartYear;

}
