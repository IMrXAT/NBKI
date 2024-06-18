package com.rodionov.nbki.platform.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CreditNotFoundException.class})
    public ResponseEntity<Object> handleEstablishmentNotFoundException(CreditNotFoundException exception, WebRequest request){
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }
}