package com.hrusch.webapp.io.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordFormatValidator implements ConstraintValidator<PasswordFormat, String> {

    @Override
    public void initialize(PasswordFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext cxt) {
        return password.matches(".*\\d+.*")
                && password.matches(".*[a-zA-Z]+.*");
    }
}
