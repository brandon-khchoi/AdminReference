package com.example.adminreference.vo;

import com.example.adminreference.exception.CustomExceptionCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Times;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorResponse {
    private int httpStatus;
    //    private int errorCode;
    private long timestamp;
    private String message;

    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(HttpStatus httpStatus, Exception e) {
        this.httpStatus = httpStatus.value();
//        this.errorCode = httpStatus.value();
        this.message = e.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(CustomExceptionCode customExceptionCode) {
        this.httpStatus = customExceptionCode.httpStatus.value();
//        this.errorCode = customExceptionCode.code;
        this.message = customExceptionCode.msg;
        this.timestamp = System.currentTimeMillis();
    }
}
