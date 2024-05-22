package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    private String fullName;
    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate dateOfBirth;
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();



}
