package com.noc.employee_cv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.enums.ForeignLang;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String language;

    @JsonIgnore
    @OneToMany(mappedBy ="language")
    private Set<EmployeeLanguage> employeeLanguages= new HashSet<>();
}
