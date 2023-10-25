package com.example.adminreference.entity;

import com.example.adminreference.dto.UserInfoDto;
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
    private String department;
    private String position;

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

    public void updatePassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void updateUserInfo(UserInfoDto.Request request) {
        this.authGroupId = request.getAuthGroupId();
        this.department = request.getDepartment();
        this.position = request.getPosition();
        this.email = request.getEmail();
        this.phone_number = request.getPhoneNumber();
    }

    public void changeAuthGroup(TbAuthGroup tbAuthGroup) {
        if (!tbAuthGroup.equals(this.tbAuthGroup)) {
            this.tbAuthGroup = tbAuthGroup;
        }
    }

    public void removeAuthGroup() {
        this.tbAuthGroup = null;
    }

    public static TbUserInfo of(String adminId, String encryptedPassword, String username, String phone_number, String email, String department, String position) {
        TbUserInfo tbUserInfo = new TbUserInfo();
        tbUserInfo.adminId = adminId;
        tbUserInfo.password = encryptedPassword;
        tbUserInfo.username = username;
        tbUserInfo.phone_number = phone_number;
        tbUserInfo.email = email;
        tbUserInfo.department = department;
        tbUserInfo.position = position;

        return tbUserInfo;
    }

    public static TbUserInfo from(UserInfoDto.Request userInfo) {
        return TbUserInfo.of(
                userInfo.getAdminId(),
                userInfo.getPassword(),
                userInfo.getUsername(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getDepartment(),
                userInfo.getPosition()
        );
    }


}
