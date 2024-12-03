package com.github.ajharry69.kcbtechnicalinterview.project.exceptions;

import com.github.ajharry69.kcbtechnicalinterview.errors.KCBException;
import org.springframework.http.HttpStatus;

public class DuplicateProjectException extends KCBException {
    public DuplicateProjectException() {
        super(HttpStatus.BAD_REQUEST, "DUPLICATE_PRODUCT");
    }
}
