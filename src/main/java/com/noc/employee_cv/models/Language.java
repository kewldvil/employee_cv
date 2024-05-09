package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.enums.ForeignLang;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private ForeignLang language;
    @JsonIgnore
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeLanguage> employeeLanguages = new ArrayList<>();
}
