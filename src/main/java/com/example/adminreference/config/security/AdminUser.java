package com.example.adminreference.config.security;

import com.example.adminreference.entity.TbUserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminUser implements UserDetails, Serializable {

    private String password;
    private final String adminId;
    private final List<RequestGrantedAuthority> authorities;

    public static AdminUser of(TbUserInfo tbUserInfo, List<RequestGrantedAuthority> authorities) {
        return AdminUser.builder()
                .adminId(tbUserInfo.getAdminId())
                .password(tbUserInfo.getPassword())
                .authorities(authorities)
                .build();
    }

    public AdminUser(String adminId, List<RequestGrantedAuthority> authorities) {
        Validate.notNull(adminId, "AdminUser.adminId는 필수 값입니다.");
        this.adminId = adminId;
        this.authorities = authorities != null ? authorities : new ArrayList<>();
    }

    @Builder(access = AccessLevel.PROTECTED)
    public AdminUser(String adminId, String password, List<RequestGrantedAuthority> authorities) {
        Validate.notNull(adminId, "AdminUser.adminId는 필수 값입니다.");
        Validate.notNull(password, "AdminUser.password는 필수 값입니다.");
        this.adminId = adminId;
        this.password = password;
        this.authorities = authorities != null ? authorities : new ArrayList<>();
    }


    @Override
    public List<RequestGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.adminId;
    }

    public String getAdminId() {
        return this.adminId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
