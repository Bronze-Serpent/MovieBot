package com.barabanov.moviebot.dto;

import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Rating;

import java.time.LocalDate;

public record FilmReadDto(Long id,
                          String title,
                          String description,
                          LocalDate releaseYear,
                          LanguageReadDto languageReadDto,
                          Integer length,
                          Rating rating,
                          Category category,
                          Integer audienceScore) { }
