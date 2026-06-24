package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpouseResponse extends ParentResponse{
    private List<ChildResponse> children;
}
