package com.example.adminreference.common.exception;

import com.example.adminreference.vo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;

@Slf4j
@RestController
@RestControllerAdvice
public class CommonExceptionAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> processValidException(BindException e) {
        log.error("BindException error", e);
        ErrorResponse responseEntity = CustomExceptionCode.MISSING_REQUIRED_VALUES.getErrorResponse();
        String message = e.getAllErrors().get(0).getDefaultMessage();

        if (e.getFieldError() != null) {
            message = e.getFieldError().getField() + ": " + e.getFieldError().getDefaultMessage();
        }
        responseEntity.setMessage(message);

        return ResponseEntity.status(responseEntity.getHttpStatus()).body(responseEntity);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> requestBodyException(HttpMessageNotReadableException e) {
        log.error("requestBodyException error", e);
        ErrorResponse responseEntity = CustomExceptionCode.BAD_REQUEST.getErrorResponse();
        responseEntity.setMessage(e.getMostSpecificCause().getMessage().substring(e.getMostSpecificCause().getMessage().indexOf("` from ") + "` from ".length(), e.getMostSpecificCause().getMessage().indexOf("\n at [")));
        return ResponseEntity.status(responseEntity.getHttpStatus()).body(responseEntity);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> validException(ValidationException e) {
        log.error("parameterException error", e);
        ErrorResponse responseEntity = CustomExceptionCode.MISSING_REQUIRED_VALUES.getErrorResponse();
        if (e instanceof ConstraintViolationException) {
            //첫번째 오류만 전달
            String message = new ArrayList<>(((ConstraintViolationException) e).getConstraintViolations()).get(0).getMessage();
            responseEntity.setMessage(message);
        } else {
            responseEntity.setMessage(e.getMessage());
        }
        return ResponseEntity.status(responseEntity.getHttpStatus()).body(responseEntity);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> parameterException(MissingServletRequestParameterException e) {
        ErrorResponse errorResponse = CustomExceptionCode.MISSING_REQUIRED_VALUES.getErrorResponse();
        errorResponse.setMessage(e.getParameterName() + ": 필수 값 미포함");

        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> securityException(Exception e) {
        log.error("securityException error", e);
        return CustomExceptionCode.ACCESS_DENIED.getResponseEntity();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> authenticationException(Exception e) {
        log.error("securityException error", e);
        return CustomExceptionCode.UNAUTHORIZED.getResponseEntity();
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(CustomException e) {
        log.error("customException", e);
        return ResponseEntity.status(e.getErrorResponse().getHttpStatus()).body(e.getErrorResponse());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> common(Exception e) {
        log.error("INTERNAL_SERVER_ERROR", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e));
    }
}
