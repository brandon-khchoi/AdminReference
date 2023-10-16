package com.example.adminreference.dto;

import com.example.adminreference.entity.TbUserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class SignupDto {

    @Setter
    @Getter
    @ToString
    public static class Request {
        private String adminId;
        private String password;
        private String username;
        private String phone_number;
        private String email;
    }


    @Setter
    @Getter
    @ToString
    public static class Response {
        private String adminId;
        private String username;
        private String phone_number;
        private String email;

        public static Response from(TbUserInfo tbUserInfo) {
            Response response = new Response();
            response.setAdminId(tbUserInfo.getAdminId());
            response.setUsername(tbUserInfo.getUsername());
            response.setPhone_number(tbUserInfo.getPhone_number());
            response.setEmail(tbUserInfo.getEmail());
            return response;
        }
    }

}
