package com.parkyc.comm_module.member.domain.entity;

import com.parkyc.comm_module.common.code.MemberStatus;
import com.parkyc.comm_module.member.domain.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="member")
@SequenceGenerator(
        name="MEMBER_SEQ_GENERATOR",
        sequenceName = "SEQ_MEMBER",
        initialValue = 1,
        allocationSize = 1
)
public class Member {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long memberId;

    @Column(name="login_id")
    private String loginId;

    @Column(name="password")
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @CreatedDate
    @Column(name="reg_dt", updatable = false)
    private LocalDateTime regDt;

    @LastModifiedDate
    @Column(name="upd_dt")
    private LocalDateTime updDt;
}
