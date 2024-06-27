package com.noc.employee_cv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DegreeLevelInfoDTO {
        private int id;
        private Boolean isChecked;
        private String educationLevel;

}
