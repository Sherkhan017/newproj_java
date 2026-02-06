package com.example.social.controller;

import com.example.social.exception.InvalidInputException;
import com.example.social.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(NotFoundException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", "NOT_FOUND",
                "message", ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBeanValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Validation failed");
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", "VALIDATION_ERROR",
                "message", message);
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidInput(InvalidInputException ex) {
        return Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", "INVALID_INPUT",
                "message", ex.getMessage());
    }
}
