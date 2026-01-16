package org.example.infra;

import org.example.exception.AuthorizationException;
import org.example.exception.BusinessRuleException;
import org.example.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BusinessRuleException.class, ValidationException.class, AuthorizationException.class})
    public ResponseEntity<Object> handleCustomExceptions(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        // Vi hittar automatiskt rätt statuskod från annotationen på klassen
        ResponseStatus status = ex.getClass().getAnnotation(ResponseStatus.class);
        return new ResponseEntity<>(body, status != null ? status.value() : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}