package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.noc.employee_cv.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table
@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
public class Father {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private boolean isAlive=false;
    private String jobName;
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
