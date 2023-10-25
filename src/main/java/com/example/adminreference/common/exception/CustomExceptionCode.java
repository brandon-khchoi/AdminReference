package com.example.adminreference.common.exception;

import com.example.adminreference.vo.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum CustomExceptionCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "BadRequest"),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, 102, "데이터를 찾을 수 없음"),
    INVALID_RESOURCE_DUPLICATE(HttpStatus.CONFLICT, 113, "중복된 리소스 형식"),
    WRONG_PASSWORD_OR_ID(HttpStatus.UNAUTHORIZED, 104, "계정정보가 없거나 잘못된 형식"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 111, "접속 권한 없음"), //임시
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 202, "Unauthorized"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 110, "Access 토큰 만료"),
    ACCESS_TOKEN_NULL_OR_EMPTY(HttpStatus.UNAUTHORIZED, 110, "요청받은 Access 토큰값 null or 빈값"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 131, "Refresh 토큰 만료"),
    MISSING_REQUIRED_VALUES(HttpStatus.BAD_REQUEST, 114, "필수값 미포함"),

    // 공통코드 추가 전
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 110, "유효한 Access Token이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 110, "유효한 Refresh Token이 존재하지 않습니다.");

    public int code;
    public String msg;
    public HttpStatus httpStatus;

    CustomExceptionCode(HttpStatus httpStatus, int code, String msg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }

    public ErrorResponse getErrorResponse() {
        return new ErrorResponse(this);
    }

    public ResponseEntity<ErrorResponse> getResponseEntity() {
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(this));
    }

}
