package com.example.adminreference.controller;

import com.example.adminreference.dto.SignupDto;
import com.example.adminreference.service.UserManageService;
import com.example.adminreference.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/manage")
public class UserManageController {

    private final UserManageService userManageService;

    @PostMapping("/register")
    public ResponseEntity<ResponseVo<SignupDto.Response>> register(
            @RequestBody SignupDto.Request signupRequest
    ) {
        ResponseVo<SignupDto.Response> responseVo = userManageService.register(signupRequest);

        return ResponseEntity.status(CREATED)
                .header("X-Affected-Row-Count", "1")
                .body(responseVo);
    }

}
