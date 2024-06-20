package com.compilou.regex.exceptions;

import java.util.Map;

public class GenericException extends RuntimeException {
    private Map<String, String> errors;

    public GenericException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}