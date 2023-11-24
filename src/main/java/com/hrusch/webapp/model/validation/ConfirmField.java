package com.hrusch.webapp.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ConfirmFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmField {

    String message() default "The fields do not match.";

    String original();

    String confirmation();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
