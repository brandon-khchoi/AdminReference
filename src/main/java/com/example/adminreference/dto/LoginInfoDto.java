package com.example.adminreference.dto;

import com.example.adminreference.entity.TbUserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@ToString
public class LoginInfoDto {
    private UserInfoDto.Response userInfo;
    private List<MenuAuthInfoDto> menuAuthInfoList;

    public void setUserInfo(UserInfoDto.Response userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserInfo(TbUserInfo tbUserInfo) {
        UserInfoDto.Response userInfoDto = new UserInfoDto.Response();
        userInfoDto.setAdminId(tbUserInfo.getAdminId());
        userInfoDto.setUsername(tbUserInfo.getUsername());
        userInfoDto.setAuthGroupName(tbUserInfo.getTbAuthGroup().getAuthGroupName());
        Optional.ofNullable(tbUserInfo.getLastLoginDt())
                .ifPresent(item -> userInfoDto.setLastLoginDt(tbUserInfo.getLastLoginDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        this.userInfo = userInfoDto;
    }

}
