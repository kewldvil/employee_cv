package com.noc.employee_cv.enums;

public enum ForeignLang implements CommonEnum{
    CHINESE("ចិន"),
    ENGLISH("អង់គ្លេស"),
    THAI("ថៃ"),
    VIETNAMESE("វៀតណាម"),
    KOREAN("កូរ៉េ"),
    JAPANESE("ជប៉ុន"),
    FRENCH("បារាំង"),
    SPANISH("អេស្ប៉ាញ"),
    GERMAN("អាល្លឺម៉ង់"),
    RUSSIAN("រុស្ស៊ី"),
    ARABIC("អារ៉ាប់"),
    PORTUGUESE("ព័រទុយហ្គាល់"),
    ITALIAN("អ៊ីតាលី");

    public final String language;

    ForeignLang(String language) {
        this.language = language;
    }


    @Override
    public String getValue() {
        return language;
    }
}
