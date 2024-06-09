package com.noc.employee_cv.dto;

import lombok.Data;

@Data
public class FileResponseDTO {
    private Integer id;
    private String name;
    private String type;
    private String base64Content;
    private String url;

}
