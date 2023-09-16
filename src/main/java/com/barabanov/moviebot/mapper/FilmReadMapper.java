package com.barabanov.moviebot.mapper;

import com.barabanov.moviebot.dto.FilmReadDto;
import com.barabanov.moviebot.entity.Film;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class FilmReadMapper implements Mapper<Film, FilmReadDto>
{
    private final LanguageReadMapper languageReadMapper;


    @Override
    public FilmReadDto mapFrom(Film object)
    {
        return new FilmReadDto(
                object.getId(),
                object.getTitle(),
                object.getDescription(),
                object.getReleaseYear(),
                languageReadMapper.mapFrom(object.getLanguage()),
                object.getLength(),
                object.getRating(),
                object.getCategory(),
                object.getAudienceScore()
        );
    }
}
