package com.compilou.regex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String ex) {
        super(ex);
    }
}