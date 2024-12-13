package com.noc.employee_cv.enums;

public enum ForeignLang implements CommonEnum{
    ENGLISH("អង់គ្លេស"),
    FRENCH("បារាំង"),
    RUSSIAN("រុស្ស៊ី"),
    CHINESE("ចិន"),
    THAI("ថៃ"),
    KOREAN("កូរ៉េ"),
    JAPANESE("ជប៉ុន"),
    VIETNAMESE("វៀតណាម"),
    LOA("ឡាវ"),
    SPANISH("អេស្ប៉ាញ"),
    GERMAN("អាល្លឺម៉ង់"),
    ARABIC("អារ៉ាប់"),
    PORTUGUESE("ព័រទុយហ្គាល់"),
    ITALIAN("អ៊ីតាលី"),
    INDONESIA("ឥណ្ឌូនេស៊ី");

    public final String language;

    ForeignLang(String language) {
        this.language = language;
    }


    @Override
    public String getValue() {
        return language;
    }
}
