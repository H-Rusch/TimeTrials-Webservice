package com.hrusch.webapp.model.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordFormat {

    String message() default "{user.password.invalidFormat}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
