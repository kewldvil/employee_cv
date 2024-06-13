package com.noc.employee_cv.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum PoliceRank implements CommonEnum {
    TRAINEE(16,"មន្ត្រីហាត់ការ"),
    CHIEF_CORPORAL(1,"ពលបាលត្រី"), STAFF_SERGEANT(2,"ពលបាលទោ"), MASTER_SERGEANT(3,"ពលបាលឯក"),
    COMMAND_SERGEANT_MAJOR(4,"ព្រឺន្ទបាលទោ"), WARRANT_OFFICER(5,"ព្រឹន្ទបាលឯក"),
    SECOND_LIEUTENANT(6,"អនុសេនីយ៍ត្រី"), FIRST_LIEUTENANT(7,"អនុសេនិយ៍ទោ"), CAPTAIN(8,"អនុសេនីយ៍ឯក"), MAJOR(9,"វរសេនីយ៍ត្រី"),
    LIEUTENANT_COLONEL(10,"វរសេនីយ៍ទោ"), COLONEL(11,"វរសេនីយ៍ឯក"),
    BRIGADIER_GENERAL(12,"ឧត្តមសេនីយ៍ត្រី"), MAJOR_GENERAL(13,"ឧត្តមសេនីយ៍ទោ"), LIEUTENANT_GENERAL(14,"ឧត្តមសេនីយ៍ឯក"),
    GENERAL(15,"នាយឧត្តមសេនីយ៍");

    @Getter
    public final String value;
    public final int key;

    PoliceRank( int key,String value) {
        this.value = value;
        this.key = key;
    }

}
