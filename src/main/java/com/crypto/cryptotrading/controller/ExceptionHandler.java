package com.crypto.cryptotrading.controller;


import com.crypto.cryptotrading.Exception.EntityNotFoundException;
import com.crypto.cryptotrading.Exception.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleMessageNotFoundException(RuntimeException exception, HttpServletRequest request) {
        LOGGER.warn("Encountered Entity not found exception", exception);
        return buildExceptionResponse(exception, request, HttpStatus.NOT_FOUND, exception.getMessage());
    }


    private ResponseEntity<ExceptionResponse> buildExceptionResponse(Exception exception, HttpServletRequest request
            , HttpStatus status, String... errorMessage) {
        ExceptionResponse body = new ExceptionResponse();
        body.setType(exception.getClass().getSimpleName());
        if (errorMessage.length > 0) {
            body.setError(errorMessage[0]);
        } else {
            body.setError(exception.getMessage());
        }

        body.setTimestamp(Instant.now().toEpochMilli());
        body.setPath(request.getRequestURI());
        return ResponseEntity
                .status(status)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body);
    }

}
