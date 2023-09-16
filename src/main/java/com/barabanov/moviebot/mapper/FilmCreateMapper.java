package com.barabanov.moviebot.mapper;

import com.barabanov.moviebot.dto.FilmCreateDto;
import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.entity.Rating;
import com.barabanov.moviebot.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class FilmCreateMapper implements Mapper<FilmCreateDto, Film>
{

    private final LanguageRepository languageRepository;

    @Override
    public Film mapFrom(FilmCreateDto object)
    {
        return new Film(
                null,
                object.title(),
                object.description(),
                object.releaseYear(),
                languageRepository.findByName(object.languageName()).orElseThrow(IllegalArgumentException::new),
                object.length(),
                Rating.fromString(object.rating()),
                Category.fromString(object.category()),
                object.audienceScore()
        );
    }
}
