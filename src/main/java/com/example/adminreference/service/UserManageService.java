package com.example.adminreference.service;

import com.example.adminreference.dto.SignupDto;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.exception.CustomException;
import com.example.adminreference.exception.CustomExceptionCode;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.vo.ErrorResponse;
import com.example.adminreference.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManageService {

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    public ResponseVo<SignupDto.Response> register(SignupDto.Request signupRequest) {
        ResponseVo<SignupDto.Response> responseVo = new ResponseVo<>();

        if (userInfoRepository.existsByAdminId(signupRequest.getAdminId())) {
            ErrorResponse response = new ErrorResponse(CustomExceptionCode.INVALID_RESOURCE_DUPLICATE);
            response.setMessage("사용중인 계정입니다.");
            throw new CustomException(response);
        }

        signupRequest.setPassword(passwordEncoder.encode("password"));
        TbUserInfo tbUserInfo = userInfoRepository.save(TbUserInfo.from(signupRequest));
        responseVo.setData(SignupDto.Response.from(tbUserInfo));
        return responseVo;
    }

}
