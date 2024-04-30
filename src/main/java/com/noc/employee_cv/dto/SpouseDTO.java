package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SpouseDTO extends ParentDTO{
    private List<ChildDTO> children;
}
