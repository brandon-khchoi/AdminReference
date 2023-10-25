package com.example.adminreference.controller;

import com.example.adminreference.config.security.AdminUser;
import com.example.adminreference.dto.LoginInfoDto;
import com.example.adminreference.common.exception.CustomException;
import com.example.adminreference.common.exception.CustomExceptionCode;
import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Tag(name = "로그인 정보 관련 API", description = "인증만 사용하고 권한은 필요없음")
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @PutMapping("/password-change")
    public ResponseEntity<Void> passwordChange(
            @AuthenticationPrincipal AdminUser adminUser,
            @RequestBody String newPassword
    ) {
        loginService.passwordChange(adminUser, newPassword);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "메뉴 및 인가 목록")
    @RequestMapping(value = "/menu-list", method = RequestMethod.GET)
    public ResponseEntity<LoginInfoDto> getMenuList(
            @AuthenticationPrincipal AdminUser adminUser
    ) {
        if (adminUser != null) {
            return ResponseEntity.ok(loginService.getMenuList(adminUser));
        } else {
            throw new CustomException(CustomExceptionCode.UNAUTHORIZED);
        }
    }
}
