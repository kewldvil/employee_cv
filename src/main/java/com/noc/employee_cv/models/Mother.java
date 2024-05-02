package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table
@Getter
@Setter
public class Mother {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private boolean isAlive=false;
    private String jobName;
//    @OneToMany(mappedBy = "mother")
//    private Set<PhoneNumber> phoneNumberList;
    private boolean isFather=true;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
