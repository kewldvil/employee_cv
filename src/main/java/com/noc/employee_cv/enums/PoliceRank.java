package com.noc.employee_cv.enums;

public enum PoliceRank {
    CHIEF_CORPORAL("ពលបាលត្រី", 1), STAFF_SERGEANT("ពលបាលទោ", 2), MASTER_SERGEANT("ពលបាលឯក", 3),
    COMMAND_SERGEANT_MAJOR("ព្រឺន្ទបាលទោ", 4), WARRANT_OFFICER("ព្រឹន្ទបាលឯក", 5),
    SECOND_LIEUTENANT("អនុសេនីយ៏ត្រី", 6), FIRST_LIEUTENANT("អនុសេនិយ៏ទោ", 7), CAPTAIN("អនុសេនីយ៏ឯក", 8), MAJOR("វរសេនីយ៏ត្រី", 9),
    LIEUTENANT_COLONEL("វរសេនីយ៏ទោ", 10), COLONEL("វរសេនីយ៏ឯក", 11),
    BRIGADIER_GENERAL("ឧត្តមសេនីយ៏ត្រី", 12), MAJOR_GENERAL("ឧត្តមសេនីយ៏ទោ", 13), LIEUTENANT_GENERAL("ឧត្តមសេនីយ៏ឯក", 14),
    GENERAL("នាយឧត្តសេនីយ៏", 15);

    public final String value;
    public final int key;

    PoliceRank(String value, int key) {
        this.value = value;

        this.key = key;
    }

}
