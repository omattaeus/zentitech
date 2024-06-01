package com.compilou.regex.exceptions;

public class CustomDataIntegrityViolationException extends RuntimeException {
    public CustomDataIntegrityViolationException(String message) {
        super(message);
    }
}
