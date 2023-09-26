package com.barabanov.moviebot.dto;

import com.barabanov.moviebot.validation.ValidateCategory;
import com.barabanov.moviebot.validation.ValidateLanguage;
import com.barabanov.moviebot.validation.ValidateRating;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record FilmCreateDto(
                            @NotNull
                            String title,
                            String description,
                            LocalDate releaseYear,

                            @ValidateLanguage
                            String languageName,
                            Integer length,

                            @ValidateRating
                            String rating,

                            @ValidateCategory
                            String category,

                            @Min(value = 0)
                            @Max(value = 10)
                            Integer audienceScore
){ }
