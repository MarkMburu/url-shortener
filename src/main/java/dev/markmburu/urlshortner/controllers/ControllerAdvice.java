package dev.markmburu.urlshortner.controllers;

import dev.markmburu.urlshortner.dtos.ApiError;
import dev.markmburu.urlshortner.exceptios.EntityExists;
import dev.markmburu.urlshortner.exceptios.EntityNotFoundException;
import java.util.HashMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Responsible for handling Validation errors.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult()
            .getAllErrors()
            .forEach(error -> {
                var fieldError = (FieldError) error;
                var fieldName = fieldError.getField();
                var message = fieldError.getDefaultMessage();

                errors.put(fieldName, message);
            });

        var apiError = new ApiError<>(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExists.class)
    ResponseEntity<Object> entityExistsHandler(EntityExists ex) {
        final var apiError = new ApiError<>(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Object> invalidCredentials(AccessDeniedException ex) {
        final var apiError = new ApiError<>(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Object> entityNotFound(EntityNotFoundException ex) {
        final var apiError = new ApiError<>(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    ResponseEntity<Object> genericExceptionHandler(Throwable ex) {
        final var apiError = new ApiError<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
