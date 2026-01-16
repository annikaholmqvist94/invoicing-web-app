package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Används när logiken säger nej (t.ex. "Kan inte radera betald faktura")
@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}