package com.compilou.regex.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CellPhoneTester.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellPhoneValidator {

    String message() default "Invalid cellphone!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
