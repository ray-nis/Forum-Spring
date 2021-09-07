package com.forum.dto.validator;

import com.forum.dto.PasswordChangeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordChangeDtoMatchesValidator implements ConstraintValidator<PasswordChangeDtoMatches, Object>  {
    @Override
    public void initialize(PasswordChangeDtoMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        PasswordChangeDto passwordChangeDto = (PasswordChangeDto) o;
        return passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword());
    }
}
