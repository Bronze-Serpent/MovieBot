package com.barabanov.moviebot.dto;


import com.barabanov.moviebot.validation.ValidateLanguage;

public record LanguageCreateDto(
                                @ValidateLanguage
                                String name
) { }
