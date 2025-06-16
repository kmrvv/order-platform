package com.example.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VerificationTokenException extends RuntimeException {
    public VerificationTokenException(String message) {
        super(message);
    }
}
