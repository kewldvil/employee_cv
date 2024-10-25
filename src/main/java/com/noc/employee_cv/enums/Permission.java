package com.noc.employee_cv.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");

//    HEAD_OF_BUREAU_READ("management:read"),
//    HEAD_OF_BUREAU_UPDATE("management:update"),
//    HEAD_OF_BUREAU_CREATE("management:create"),
//    HEAD_OF_BUREAU_DELETE("management:delete");

    private final String permission;
}
