package com.example.adminreference.dto;

import com.example.adminreference.entity.TbUserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserInfoDto {

    @Setter
    @Getter
    @ToString
    public static class Request {
        private String adminId;
        private String password;
        private String username;
        private String phoneNumber;
        private String email;
        private String department;
        private String position;
        private Long authGroupId;

        private String passwordReset;
    }


    @Setter
    @Getter
    @ToString
    public static class Response {
        private Long id;
        private String adminId;
        private String username;
        private String email;
        private String phoneNumber;
        private String department;
        private String position;
        private Long authGroupId;
        private String authGroupName;
        private String lastLoginDt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regDt;

        public static UserInfoDto.Response from(TbUserInfo tbUserInfo) {
            UserInfoDto.Response response = new UserInfoDto.Response();
            response.setId(tbUserInfo.getId());
            response.setAdminId(tbUserInfo.getAdminId());
            response.setUsername(tbUserInfo.getUsername());
            response.setEmail(tbUserInfo.getEmail());
            response.setPhoneNumber(tbUserInfo.getPhone_number());
            response.setDepartment(tbUserInfo.getDepartment());
            response.setPosition(tbUserInfo.getPosition());
            response.setAuthGroupId(tbUserInfo.getAuthGroupId());
            response.setAuthGroupName(tbUserInfo.getTbAuthGroup().getAuthGroupName());

            Optional.ofNullable(tbUserInfo.getLastLoginDt())
                    .ifPresentOrElse(
                            item -> response.setLastLoginDt(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                            () -> response.setLastLoginDt("-")
                    );
            return response;
        }
    }

}
