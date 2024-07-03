package com.noc.employee_cv.enums;

public enum UniversityMajor implements CommonEnum{
    IT("ព័ត៍មានវិទ្យា"),
    MECHANIC("មេកានិច"),
    LAW("ច្បាប់"),
    MARKETING("ទីផ្សារ"),
    MANAGEMENT("គ្រប់គ្រង"),
    CIVIL_ENGINEER("វិស្វករសំណង់ស៊ីវិល"),
    ACCOUNTING("គណនេយ្យ"),
    ELECTRIC_ENGINEER("វិស្វករអគ្គិសនី"),
    ARCHITECTURE("ស្ថាបតិយ្យករ"),
    BANK("ធនាគារ"),
    ECONOMIC("សេដ្ឋកិច្ច"),
    AGRICULTURE("កសិកម្ម"),
    MIS("គ្រប់គ្រងព័ត៍មានវិទ្យា"),
    PUBLIC_ADMINISTRATION("រដ្ឋបាលសាធារណៈ"),
    DOCTOR("ពេទ្យ"),
    ENG_LANG("អក្សរសាស្ត្រអង់គ្លេស"),
    FRANCE_LANG("អក្សរសាស្ត្របារាំង"),
    FINANCE("ហិរញ្ញវត្ថុ"),
    FOREIGN_AFFAIR("ទំនាក់ទំនងការបរទេស");

    public final String majorSkill;

    UniversityMajor(String majorSkill) {
        this.majorSkill = majorSkill;
    }

    @Override
    public String getValue() {
        return majorSkill;
    }
}
