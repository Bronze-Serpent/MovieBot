package com.barabanov.moviebot.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = LanguageValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateLanguage
{
    String message() default "The language line must be in the database";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
