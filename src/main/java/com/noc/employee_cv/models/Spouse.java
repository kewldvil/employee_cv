package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noc.employee_cv.dto.PhoneNumberDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Spouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String spouseFullName;
    private boolean isAlive=true;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate spouseDateOfBirth;
    private String spouseJobName;


    @OneToMany(mappedBy = "spouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SpouseChildren> children=new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
