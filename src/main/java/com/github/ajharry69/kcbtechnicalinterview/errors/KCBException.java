package com.github.ajharry69.kcbtechnicalinterview.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KCBException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public KCBException(HttpStatus httpStatus, String errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}

