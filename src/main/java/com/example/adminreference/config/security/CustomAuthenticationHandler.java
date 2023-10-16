package com.example.adminreference.config.security;

import com.example.adminreference.dto.LoginResponse;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.enumeration.AccountState;
import com.example.adminreference.repository.UserInfoRepository;
import com.example.adminreference.vo.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    private final UserInfoRepository userInfoRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        AdminUser adminUser = (AdminUser) authentication.getPrincipal();
        String token = jwtProvider.createToken(adminUser);

        Optional<TbUserInfo> tbUserInfo = userInfoRepository.findByAdminId(adminUser.getUsername());

        if (tbUserInfo.isPresent()) {
            tbUserInfo.get().updateLastLoginDt();
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setUsername(adminUser.getUsername());
            objectMapper.writeValue(response.getWriter(), loginResponse);
        } else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUsername(adminUser.getUsername());
            objectMapper.writeValue(response.getWriter(), loginResponse);
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        log.info(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.UNAUTHORIZED.value());
//            errorResponse.setErrorCode(104);
        errorResponse.setMessage("로그인에 실패하였습니다. 계정 및 비밀번호를 확인해주세요.");

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
