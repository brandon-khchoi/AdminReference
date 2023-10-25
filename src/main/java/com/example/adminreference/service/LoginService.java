package com.example.adminreference.service;

import com.example.adminreference.common.exception.CustomException;
import com.example.adminreference.common.exception.CustomExceptionCode;
import com.example.adminreference.config.security.AdminUser;
import com.example.adminreference.config.security.RequestGrantedAuthority;
import com.example.adminreference.dto.LoginInfoDto;
import com.example.adminreference.dto.MenuAuthInfoDto;
import com.example.adminreference.dto.SubMenuAuthInfoDto;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.repository.querydsl.UserMenuInfoCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserInfoRepository userInfoRepository;

    private final UserMenuInfoCustomRepository userMenuInfoCustomRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void passwordChange(AdminUser adminUser, String newPassword) {
        TbUserInfo tbUserInfo = userInfoRepository.findByAdminId(adminUser.getAdminId())
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));

        if (newPassword.length() < 8 || newPassword.length() > 16) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "8~16자의 숫자와 영문자, 또는 특수문자를 사용하여 입력해주세요.");
        } else if (passwordEncoder.matches(newPassword, tbUserInfo.getPassword())) {
            throw new CustomException(HttpStatus.CONFLICT, "기존 패스워드와 동일합니다.");
        }
        tbUserInfo.updatePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional(readOnly = true)
    public LoginInfoDto getMenuList(AdminUser adminUser) {
        LoginInfoDto userAuthInfo = new LoginInfoDto();
        userAuthInfo.setUserInfo(userMenuInfoCustomRepository.selectTbUserInfo(adminUser.getAdminId()));
        userAuthInfo.setMenuAuthInfoList(userMenuInfoCustomRepository.selectMenuAuthByAdminId(adminUser.getAdminId()));

        return userAuthInfo;
    }

    public List<RequestGrantedAuthority> getAuthorityList(String adminId) {
        Set<RequestGrantedAuthority> authoritySet = new HashSet<>();
        List<MenuAuthInfoDto> menuAuthInfoDtoList = userMenuInfoCustomRepository.selectMenuAuthByAdminId(adminId);
        if (menuAuthInfoDtoList != null && !menuAuthInfoDtoList.isEmpty()) {
            for (MenuAuthInfoDto menuAuthInfoDto : menuAuthInfoDtoList) {
                for (SubMenuAuthInfoDto subMenuAuthInfoDto : menuAuthInfoDto.getSubMenuAuthInfoList()) {
                    RequestGrantedAuthority requestGrantedAuthority = new RequestGrantedAuthority(subMenuAuthInfoDto.getMenuUrl(), subMenuAuthInfoDto.getPermissionNames());
                    authoritySet.add(requestGrantedAuthority);
                }
            }
        }
        return new ArrayList<>(authoritySet);
    }

    @Override
    public AdminUser loadUserByUsername(String adminId) throws UsernameNotFoundException {
        TbUserInfo tbUserInfo = userInfoRepository.findByAdminId(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user " + adminId));
        List<RequestGrantedAuthority> authorityList = getAuthorityList(tbUserInfo.getAdminId());
        return AdminUser.of(tbUserInfo, authorityList);
    }

}
