package com.example.adminreference.common.exception;


import com.example.adminreference.vo.ErrorResponse;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final ErrorResponse errorResponse;

    public CustomException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public CustomException(HttpStatus httpStatus, int errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(httpStatus.value());
        errorResponse.setErrorCode(errorCode);
        errorResponse.setMessage(message);
        this.errorResponse = errorResponse;
    }
    public CustomException(HttpStatus httpStatus, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(httpStatus.value());
        errorResponse.setErrorCode(httpStatus.value());
        errorResponse.setMessage(message);
        this.errorResponse = errorResponse;
    }


    public CustomException(CustomExceptionCode customExceptionCode) {
        this.errorResponse = customExceptionCode.getErrorResponse();
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
