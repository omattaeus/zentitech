package com.compilou.regex.interfaces;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailTester implements ConstraintValidator<EmailValidator, String> {

    private static final String verifyEmail = "([a-z0-9\\.\\-]{2,})@([a-z0-9]{2,})((\\.[a-z]{2,})+)";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Pattern emailRegex = Pattern.compile(verifyEmail);
        Matcher compare = emailRegex.matcher(s);

        return compare.find();
    }
}
