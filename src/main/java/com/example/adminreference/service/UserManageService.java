package com.example.adminreference.service;

import com.example.adminreference.common.exception.CustomException;
import com.example.adminreference.common.exception.CustomExceptionCode;
import com.example.adminreference.dto.UserInfoDto;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.enumeration.UserSearchType;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.repository.querydsl.UserMenuInfoCustomRepository;
import com.example.adminreference.vo.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManageService {

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMenuInfoCustomRepository userMenuInfoCustomRepository;

    @Value("${init.password}")
    private String initPassword;

//    @Transactional
//    public void passwordReset(String adminId) {
//        TbUserInfo tbUserInfo = userInfoRepository.findByAdminId(adminId)
//                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));
//        tbUserInfo.updatePassword(passwordEncoder.encode(initPassword));
//    }

    public void userRegister(UserInfoDto.Request request) {
        if (userInfoRepository.existsByAdminId(request.getAdminId())) {
            ErrorResponse response = new ErrorResponse(CustomExceptionCode.INVALID_RESOURCE_DUPLICATE);
            response.setMessage("[" + request.getAdminId() + "] 현재 사용중인 계정입니다.");
            throw new CustomException(response);
        }

        request.setPassword(passwordEncoder.encode(initPassword));
        userInfoRepository.save(TbUserInfo.from(request));
    }

    @Transactional
    public UserInfoDto.Response userModify(long id, UserInfoDto.Request request) {
        TbUserInfo tbUserInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.NOT_FOUND_DATA));

        if ("Y".equals(request.getPasswordReset())) {
            tbUserInfo.updatePassword(passwordEncoder.encode(initPassword));
        }
        tbUserInfo.updateUserInfo(request);

        return UserInfoDto.Response.from(tbUserInfo);
    }


    @Transactional(readOnly = true)
    public PageImpl<UserInfoDto.Response> getAllUserList(UserSearchType searchType, String searchText, Long authGroupId, int page, int pageSize) {
        return userMenuInfoCustomRepository.selectAllUserAuthInfo(searchType, searchText, authGroupId, PageRequest.of(page - 1, pageSize));
    }

    @Transactional(readOnly = true)
    public UserInfoDto.Response getMenuList(long id) {

        UserInfoDto.Response userInfo = userMenuInfoCustomRepository.selectUserAuthGroupInfo(id);
        if (userInfo == null) {
            throw new CustomException(CustomExceptionCode.NOT_FOUND_DATA);
        }
        return userInfo;
    }

}
