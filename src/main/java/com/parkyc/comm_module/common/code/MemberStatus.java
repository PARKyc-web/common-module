package com.parkyc.comm_module.common.code;

import lombok.Getter;

@Getter
public enum MemberStatus {
    ACTIVE("00"),
    INACTIVE("01"),
    NOT_VERIFIED("02"),
    LOCK("03"),
    DROP("04");

    private final String statusCode;

    MemberStatus(String statusCode) {
        this.statusCode = statusCode;
    }
}
