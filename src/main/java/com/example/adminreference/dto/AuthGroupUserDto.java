package com.example.adminreference.dto;

import com.example.adminreference.entity.TbUserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AuthGroupUserDto {
    private Long id;
    private String adminId;
    private String username;
    private String department;
    private String position;
    private String email;
    private LocalDateTime regDt;

    public static AuthGroupUserDto of(TbUserInfo tbUserInfo) {
        AuthGroupUserDto authGroupUserDto = new AuthGroupUserDto();
        authGroupUserDto.id= tbUserInfo.getId();
        authGroupUserDto.adminId = tbUserInfo.getAdminId();
        authGroupUserDto.username = tbUserInfo.getUsername();
        authGroupUserDto.department = tbUserInfo.getDepartment();
        authGroupUserDto.position = tbUserInfo.getPosition();
        authGroupUserDto.regDt = tbUserInfo.getRegDt();
        return authGroupUserDto;
    }
}
