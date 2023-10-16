package com.example.adminreference.exception;


import com.example.adminreference.vo.ErrorResponse;

public class CustomException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public CustomException(CustomExceptionCode customExceptionCode) {
        this.errorResponse = customExceptionCode.getErrorResponse();
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
