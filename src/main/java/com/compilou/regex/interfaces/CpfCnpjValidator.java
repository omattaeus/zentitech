package com.compilou.regex.interfaces;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CpfCnpjTester.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfCnpjValidator {
    String message() default "Invalid CPF/CNPJ!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}