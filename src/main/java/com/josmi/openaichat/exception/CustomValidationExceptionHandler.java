package com.josmi.openaichat.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class CustomValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
                MethodArgumentNotValidException ex,
                HttpServletRequest request
            ) throws MethodArgumentNotValidException {
        if(isAnnotatedWithHandleValidationErrors(ex)) {
            List<String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            ValidationErrorResponse response = ValidationErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .errors(errors)
                    .path(request.getRequestURI())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
        throw ex;
    }

    private boolean isAnnotatedWithHandleValidationErrors(MethodArgumentNotValidException ex) {
        return Objects.requireNonNull(ex.getParameter().getMethod()).isAnnotationPresent(HandleValidationErrors.class);
    }
}
