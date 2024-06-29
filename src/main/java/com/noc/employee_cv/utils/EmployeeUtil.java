package com.noc.employee_cv.utils;

import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.Skill;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeUtil {
    public static String updateUniversitySkill(Employee e){
        if (e == null || e.getSkills() == null || e.getSkills().isEmpty()) {
            return "";
        }

        List<String> skillNames = e.getSkills().stream()
                .map(Skill::getSkillName)
                .collect(Collectors.toList());

        return String.join(" / ", skillNames);
    }
    public static String updateForeignLanguage(Employee employee) {
        if (employee == null || employee.getEmployeeLanguages() == null || employee.getEmployeeLanguages().isEmpty()) {
            return "";
        }

        List<String> languageStrings = employee.getEmployeeLanguages().stream()
                .filter(foreignLanguage -> foreignLanguage.getLanguage() != null && foreignLanguage.getLevel() != null && !foreignLanguage.getLevel().isEmpty())
                .map(foreignLanguage -> "(" + foreignLanguage.getLanguage().getLanguage() + " - " + foreignLanguage.getLevel() + ")")
                .collect(Collectors.toList());

        return String.join(" / ", languageStrings);
    }
}
