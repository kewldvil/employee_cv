package com.noc.employee_cv.records;

public record VillageRecord(int id, int commune_id, String village_code, String village_name_kh, String village_name_en,
                            boolean enabled) {
}
