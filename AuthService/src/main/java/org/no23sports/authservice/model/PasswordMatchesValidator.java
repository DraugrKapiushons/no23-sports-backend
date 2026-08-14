package org.no23sports.authservice.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegistration> {

    @Override
    public boolean isValid(UserRegistration request, ConstraintValidatorContext context) {
        boolean valid = request.getPassword() != null &&
                         request.getPassword().equals(request.getPasswordConfirm());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                   .addPropertyNode("passwordConfirm")
                   .addConstraintViolation();
        }
        return valid;
    }
}