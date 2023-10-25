package com.example.adminreference.vo;

import com.example.adminreference.common.exception.CustomExceptionCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorResponse {

    private int httpStatus;
    private int errorCode;
    private long timestamp;
    private String message;

    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(HttpStatus httpStatus, Exception e) {
        this.httpStatus = httpStatus.value();
        this.errorCode = httpStatus.value();
        this.message = e.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(CustomExceptionCode customExceptionCode) {
        this.httpStatus = customExceptionCode.httpStatus.value();
        this.errorCode = customExceptionCode.code;
        this.message = customExceptionCode.msg;
        this.timestamp = System.currentTimeMillis();
    }
}
