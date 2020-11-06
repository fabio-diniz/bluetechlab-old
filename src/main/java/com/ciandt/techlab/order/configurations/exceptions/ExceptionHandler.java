package com.ciandt.techlab.order.configurations.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .badRequest()
                .body(
                        new Error(
                                status.value(),
                                extractFieldError(ex.getBindingResult()),
                                status.getReasonPhrase()
                        )
                );
    }

    private String extractFieldError(final BindingResult bindingResult) {
        return Objects.requireNonNull(bindingResult.getFieldError()).getField() +
                " - " +
                Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
    }

    @Getter
    @AllArgsConstructor
    @Builder(toBuilder = true)
    static class Error {
        private int status;
        private String message;
        private String statusDescription;
    }
}
