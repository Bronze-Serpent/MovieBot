package com.barabanov.moviebot.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = RatingValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateRating
{
    String message() default "The rating string have to corresponds to the object Rating";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
