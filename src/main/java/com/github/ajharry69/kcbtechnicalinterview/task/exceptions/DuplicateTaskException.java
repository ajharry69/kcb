package com.github.ajharry69.kcbtechnicalinterview.task.exceptions;

import com.github.ajharry69.kcbtechnicalinterview.errors.KCBException;
import org.springframework.http.HttpStatus;

public class DuplicateTaskException extends KCBException {
    public DuplicateTaskException() {
        super(HttpStatus.BAD_REQUEST, "DUPLICATE_PRODUCT");
    }
}
