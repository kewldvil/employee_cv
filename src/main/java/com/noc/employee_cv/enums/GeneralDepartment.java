package com.noc.employee_cv.enums;

public enum GeneralDepartment implements CommonEnum{
    COMMISSARIAT_OF_NATIONAL_POLICE("អគ្គស្នងការដ្ឋាននគរបាលជាតិ"),
    GENERAL_DEPARTMENT_OF_INSPECTION("អគ្គាធិការដ្ឋាន"),
    DIRECTORATE_GENERAL_OF_PRISONS("អគ្គនាយកដ្ឋានពន្ធនាគារ"),
    GENERAL_DEPARTMENT_OF_IMMIGRATION("អគ្គនាយកដ្ឋានអន្តោប្រវេសន៍"),
    GENERAL_DEPARTMENT_OF_IDENTIFICATION("អគ្គនាយកដ្ឋានអត្តសញ្ញាណកម្ម"),
    GENERAL_DEPARTMENT_OF_LOGISTICS_AND_FINANCES("អគ្គនាយកដ្ឋានភស្តុភារ និងហិរញ្ញវត្ថុ"),
    POLICE_ACADEMY_OF_CAMBODIA("បណ្ឌិតសភានគរបាលកម្ពុជា"),
    GENERAL_DEPARTMENT_OF_INTERNAL_AUDIT("អគ្គនាយកដ្ឋានសាវនកម្មផ្ទៃក្នុង"),
    LEGAL_COUNCIL("ក្រុមប្រឹក្សានីតិកម្ម"),
    NATIONAL_SCHOOL_OF_LOCAL_ADMINISTRATION("សាលាជាតិរដ្ឋបាលមូលដ្ឋាន"),
    GENERAL_SECRETARIAT("អគ្គលេខាធិការដ្ឋាន"),
    GENERAL_DEPARTMENT_OF_ADMINISTRATION("អគ្គនាយកដ្ឋានរដ្ឋបាល"),
    GENERAL_DEPARTMENT_OF_DIGITAL_TECHNOLOGY_AND_MEDIA("អគ្គនាយកដ្ឋានបច្ចេកវិទ្យាឌីជីថល និងផ្សព្វផ្សាយអប់រំ");

    public final String departmentName;


    GeneralDepartment(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String getValue() {
        return departmentName;
    }
}
