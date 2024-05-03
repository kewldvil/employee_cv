package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class SpouseChildren {
    @ManyToOne()
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String childFullName;
    private Gender childGender;
    private LocalDate childDateOfBirth;
    private String childJob;
}
