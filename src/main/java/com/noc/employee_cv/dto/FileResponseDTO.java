package com.noc.employee_cv.dto;

import lombok.Data;

@Data
public class FileResponseDTO {
    private String fileName;
    private String fileType;
    private String base64Content;
    private String fileUrl;

}
