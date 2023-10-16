package com.example.adminreference.entity;

import com.example.adminreference.dto.SignupDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user_info")
public class TbUserInfo {

    @Id
    private long id;
    private String adminId;
    private String password;
    private String username;
    private String phone_number;
    private String email;

    @Column(updatable = false, insertable = false)
    private Long authGroupId;
    private LocalDateTime lastLoginDt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;
    @Column(insertable = false)
    private LocalDateTime updDt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authGroupId")
    private TbAuthGroup tbAuthGroup;

    public void updateLastLoginDt() {
        this.lastLoginDt = LocalDateTime.now();
    }

    public static TbUserInfo of(String adminId, String encodedPassword, String userName, String phone_number, String email) {
        TbUserInfo tbUserInfo = new TbUserInfo();
        tbUserInfo.adminId = adminId;
        tbUserInfo.password = encodedPassword;
        tbUserInfo.username = userName;
        tbUserInfo.phone_number = phone_number;
        tbUserInfo.email = email;

        return tbUserInfo;
    }

    public static TbUserInfo from(SignupDto.Request signupRequest) {
        return TbUserInfo.of(
                signupRequest.getAdminId(),
                signupRequest.getPassword(),
                signupRequest.getUsername(),
                signupRequest.getPhone_number(),
                signupRequest.getEmail()
        );
    }


}
