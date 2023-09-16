package com.barabanov.moviebot.mapper;

import com.barabanov.moviebot.dto.LanguageCreateDto;
import com.barabanov.moviebot.entity.Language;

import java.util.ArrayList;

public class LanguageCreateMapper implements Mapper<LanguageCreateDto, Language>
{
    @Override
    public Language mapFrom(LanguageCreateDto object)
    {
        return new Language(null, object.name(), new ArrayList<>());
    }
}
