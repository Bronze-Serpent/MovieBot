package com.barabanov.moviebot.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class LanguageValidator implements ConstraintValidator<ValidateLanguage, String>
{
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        // тут из БД доставать все Language.name и сверять бы, чтобы такой точно был
        return true;
    }
}
