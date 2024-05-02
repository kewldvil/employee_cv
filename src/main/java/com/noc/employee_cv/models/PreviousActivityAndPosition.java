package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table
public class PreviousActivityAndPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fromDateToDateActivity;
    private String activityAndAchievement;

    private String department;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


}
