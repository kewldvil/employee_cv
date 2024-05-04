package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.ForeignLang;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private ForeignLang language;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeLanguage> employeeLanguages=new HashSet<>();
}
