package com.example.adminreference.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {
    private String username;
    private String passwordReset;
    private String token;
}
