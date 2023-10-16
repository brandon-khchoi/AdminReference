package com.example.adminreference.controller;

import com.example.adminreference.config.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class JwkExposeController {

    private final JwtProvider jwtProvider;

    @GetMapping(".well-known/jwks.json")
    public String exposeJWK() {
        return jwtProvider.constructJWK();
    }

}
