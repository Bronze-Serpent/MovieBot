package com.barabanov.moviebot.validation;

import com.barabanov.moviebot.entity.Category;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CategoryValidator implements ConstraintValidator<ValidateRating, String>
{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        return Category.isItCategory(value);
    }
}
