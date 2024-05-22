package com.noc.employee_cv.enums;

public enum SkillLevel implements CommonEnum{
    POOR("ខ្សោយ"), FAIR("មធ្យម"),
    GOOD("ល្អ"), VERY_GOOD("ល្អណាស់");

    public final String levelName;

    SkillLevel(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public String getValue() {
        return levelName;
    }
}
