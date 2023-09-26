package com.barabanov.moviebot.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class LanguageValidator implements ConstraintValidator<ValidateLanguage, String>
{

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        // TODO: 26.09.2023 чтобы тут сделать валидвацию на проверку наличия языка с таким именем в БД нужен languageService,
        //  но объект LanguageValidator создаётся автоматически. Короче нужен ContextProvider, а тут его нет :)
        return true;
    }
}
