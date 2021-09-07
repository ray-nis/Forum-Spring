package com.forum.dto.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordChangeDtoMatchesValidator.class)
@Documented
public @interface PasswordChangeDtoMatches {
    String message() default "{valiationPasswordDontMatch}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
