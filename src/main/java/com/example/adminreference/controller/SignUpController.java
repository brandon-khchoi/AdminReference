package com.example.adminreference.controller;

import com.example.adminreference.dto.SignupDto;
import com.example.adminreference.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<SignupDto.Response> signup(
            @RequestBody SignupDto.Request signupRequest
    ) {
        SignupDto.Response signupResponse = signupService.signup(signupRequest);

        return ResponseEntity.ok()
                .header("X-Affected-Row-Count", "1")
                .body(signupResponse);
    }
}
