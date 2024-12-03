package com.github.ajharry69.kcbtechnicalinterview.task.exceptions;

import com.github.ajharry69.kcbtechnicalinterview.errors.KCBException;
import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends KCBException {
    public TaskNotFoundException() {
        super(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND");
    }
}
