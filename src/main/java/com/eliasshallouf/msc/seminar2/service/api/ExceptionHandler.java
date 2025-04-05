package com.eliasshallouf.msc.seminar2.service.api;

import com.eliasshallouf.msc.seminar2.domain.model.exceptions.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({
        RuntimeException.class
    })
    protected ResponseEntity<Object> handleGeneralError(Exception ex, WebRequest request) {
        return handleExceptionInternal(
                ex,
                new ExceptionResponse(ex.getMessage()),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }
}