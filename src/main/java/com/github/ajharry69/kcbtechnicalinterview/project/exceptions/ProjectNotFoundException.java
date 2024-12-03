package com.github.ajharry69.kcbtechnicalinterview.project.exceptions;

import com.github.ajharry69.kcbtechnicalinterview.errors.KCBException;
import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends KCBException {
    public ProjectNotFoundException() {
        super(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND");
    }
}
