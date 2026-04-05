package com.parkyc.comm_module.member.domain.dto;

import com.parkyc.comm_module.common.code.MemberStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class MemberDTO {

    ///  모든 회원정보
    @Data
    @Builder
    public static class All {
        private String memberId;
        private String loginId;
        private String password;
        private String status;
        private LocalDateTime regDt;
        private LocalDateTime updDt;
    }

    ///  회원가입 시 사용 DTO
    @Data
    @Builder
    public static class SignUp {
        private String loginId;
        private String password;
        /// add Any Columns What You Need.
    }
}
