package com.barabanov.moviebot.mapper;

import com.barabanov.moviebot.dto.LanguageReadDto;
import com.barabanov.moviebot.entity.Language;

public class LanguageReadMapper implements Mapper<Language, LanguageReadDto>
{
    @Override
    public LanguageReadDto mapFrom(Language object) {
        return new LanguageReadDto(
                object.getId(),
                object.getName());
    }
}
