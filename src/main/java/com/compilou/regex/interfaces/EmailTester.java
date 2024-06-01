package com.compilou.regex.interfaces;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailTester implements ConstraintValidator<EmailValidator, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        String verifyEmail = "([a-z0-9\\.\\-]{2,})@([a-z0-9]{2,})((\\.[a-z]{2,})+)";

        Pattern emailRegex = Pattern.compile(verifyEmail);
        Matcher compare = emailRegex.matcher(s);

        if(compare.find()) return true;
        else return false;
    }
}
