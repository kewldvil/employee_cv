package com.noc.employee_cv.enums;

public enum PoliceRank {
    CHIEF_CORPORAL("ពលបាលត្រី"), STAFF_SERGEANT("ពលបាលទោ"), MASTER_SERGEANT("ពលបាលឯក"),
    COMMAND_SERGEANT_MAJOR("ព្រឺន្ទបាលទោ"), WARRANT_OFFICER("ព្រឹន្ទបាលឯក"),
    SECOND_LIEUTENANT("អនុសេនីយ៏ត្រី"), FIRST_LIEUTENANT("អនុសេនិយ៏ទោ"), CAPTAIN("អនុសេនីយ៏ឯក"), MAJOR("វរសេនីយ៏ត្រី"),
    LIEUTENANT_COLONEL("វរសេនីយ៏ទោ"), COLONEL("វរសេនីយ៏ឯក"),
    BRIGADIER_GENERAL("ឧត្តមសេនីយ៏ត្រី"), MAJOR_GENERAL("ឧត្តមសេនីយ៏ទោ"), LIEUTENANT_GENERAL("ឧត្តមសេនីយ៏ឯក"),
    GENERAL("នាយឧត្តសេនីយ៏");

    public final String value;


    PoliceRank(String value) {
        this.value = value;

    }

}
