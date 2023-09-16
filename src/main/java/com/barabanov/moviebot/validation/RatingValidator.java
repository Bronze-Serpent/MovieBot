package com.barabanov.moviebot.validation;

import com.barabanov.moviebot.entity.Rating;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class RatingValidator implements ConstraintValidator<ValidateRating, String>
{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        return Rating.isItRating(value);
    }
}
