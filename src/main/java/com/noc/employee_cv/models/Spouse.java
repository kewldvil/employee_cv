package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noc.employee_cv.dto.PhoneNumberDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table
public class Spouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String spouseFullName;
    private boolean isAlive=true;
    private String spouseGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate spouseDateOfBirth;
    private String spouseJobName;

    @OneToMany(mappedBy = "spouse")
    private Set<SpouseChildren> children;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
