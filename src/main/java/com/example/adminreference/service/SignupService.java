package com.example.adminreference.service;

import com.example.adminreference.dto.SignupDto;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.exception.CustomException;
import com.example.adminreference.exception.CustomExceptionCode;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.vo.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserInfoRepository userInfoRepository;

    public SignupDto.Response signup(SignupDto.Request signupRequest) {

        if (userInfoRepository.existsByAdminId(signupRequest.getAdminId())) {
            ErrorResponse response = new ErrorResponse(CustomExceptionCode.INVALID_RESOURCE_DUPLICATE);
            response.setMessage("사용중인 계정입니다.");
            throw new CustomException(response);
        }

        TbUserInfo tbUserInfo = userInfoRepository.save(TbUserInfo.from(signupRequest));
        return SignupDto.Response.from(tbUserInfo);
    }

}
