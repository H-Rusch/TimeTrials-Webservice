package com.hrusch.webapp.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class ConfirmFieldValidator implements ConstraintValidator<ConfirmField, Object> {

    private String message;
    private String originalField;
    private String confirmationField;

    @Override
    public void initialize(ConfirmField constraintAnnotation) {
        message = constraintAnnotation.message();
        originalField = constraintAnnotation.original();
        confirmationField = constraintAnnotation.confirmation();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        Object field1Value = wrapper.getPropertyValue(originalField);
        Object field2Value = wrapper.getPropertyValue(confirmationField);

        boolean valid = field1Value != null && field1Value.equals(field2Value);

        if (!valid) {
            configureContext(context);
        }

        return valid;
    }

    private void configureContext(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(confirmationField)
                .addConstraintViolation();
    }
}
