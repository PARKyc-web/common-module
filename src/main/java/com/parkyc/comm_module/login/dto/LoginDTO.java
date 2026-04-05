package com.parkyc.comm_module.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginDTO {

    public enum Code {
        SUCCESS, FAIL, WAIT, LOCK
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String loginId;
        private String password;
    }

    @Data
    @Builder
    public static class Response {
        private LoginDTO.Code result; /* 01: 로그인 실패, 02: 로그인 성공, 03: 만료계정, ... 기타 추가 가능 */
        private String loginId;
        private String accessToken;
        private String refreshToken;

    }

    @Data
    @Builder
    public static class Member {
        private String loginId;
        private String status;
    }
}
