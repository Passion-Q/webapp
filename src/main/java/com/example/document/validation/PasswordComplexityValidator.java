package com.example.document.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexity, String> {

    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String LOWERCASE_REGEX = ".*[a-z].*";
    private static final String DIGIT_REGEX = ".*[0-9].*";
    private static final String SPECIAL_CHAR_REGEX = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";

    @Override
    public void initialize(PasswordComplexity constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true;
        }

        boolean hasUppercase = password.matches(UPPERCASE_REGEX);
        boolean hasLowercase = password.matches(LOWERCASE_REGEX);
        boolean hasDigit = password.matches(DIGIT_REGEX);
        boolean hasSpecialChar = password.matches(SPECIAL_CHAR_REGEX);

        if (!hasUppercase || !hasLowercase || !hasDigit || !hasSpecialChar) {
            context.disableDefaultConstraintViolation();
            StringBuilder message = new StringBuilder("密码必须包含：");
            if (!hasUppercase) message.append("大写字母 ");
            if (!hasLowercase) message.append("小写字母 ");
            if (!hasDigit) message.append("数字 ");
            if (!hasSpecialChar) message.append("特殊字符(!@#$%^&*等)");
            context.buildConstraintViolationWithTemplate(message.toString())
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}