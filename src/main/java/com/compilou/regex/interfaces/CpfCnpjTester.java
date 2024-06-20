package com.compilou.regex.interfaces;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CpfCnpjTester implements ConstraintValidator<CpfCnpjValidator, String> {
    private final String verifyCpfCnpj = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Pattern pattern = Pattern.compile(verifyCpfCnpj);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}