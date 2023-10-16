package com.example.adminreference.service;

import com.example.adminreference.config.security.AdminUser;
import com.example.adminreference.config.security.RequestGrantedAuthority;
import com.example.adminreference.dto.MenuAuthInfoDto;
import com.example.adminreference.dto.SubMenuAuthInfoDto;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.repository.UserMenuInfoCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    private final HttpServletRequest request;
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
