package com.noc.employee_cv.enums;

public enum UniversityMajor implements CommonEnum{
    IT("ព័ត៍មានវិទ្យា"),
    MECHANIC("មេកានិច"),
    LAW("ច្បាប់"),
    MARKETING("ទីផ្សារ"),
    MANAGEMENT("គ្រប់គ្រង់"),
    CIVIL_ENGINEER("វិស្វករសំណង់ស៊ីវិល"),
    ACCOUNTING("គណនេយ្យ"),
    ELECTRIC_ENGINEER("វិស្វករអគ្គិសនី"),
    ARCHITECTURE("ស្ថាបតិយ្យករ"),
    BANK("ធនាគារ"),
    ECONOMIC("សេដ្ឋកិច្ច"),
    AGRICULTURE("កសិកម្ម"),
    MIS("គ្រប់គ្រងព័ត៍មានវិទ្យា");

    public final String majorSkill;

    UniversityMajor(String majorSkill) {
        this.majorSkill = majorSkill;
    }

    @Override
    public String getValue() {
        return majorSkill;
    }
}
