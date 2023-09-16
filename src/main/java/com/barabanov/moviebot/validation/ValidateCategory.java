package com.barabanov.moviebot.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = CategoryValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateCategory
{
    String message() default "The category string have to corresponds to the object Category";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
