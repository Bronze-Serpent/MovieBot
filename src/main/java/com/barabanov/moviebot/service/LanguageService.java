package com.barabanov.moviebot.service;

import com.barabanov.moviebot.dto.LanguageCreateDto;
import com.barabanov.moviebot.dto.LanguageReadDto;
import com.barabanov.moviebot.entity.Language;
import com.barabanov.moviebot.mapper.LanguageCreateMapper;
import com.barabanov.moviebot.mapper.LanguageReadMapper;
import com.barabanov.moviebot.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;


@RequiredArgsConstructor
public class LanguageService
{
    private final Validator validator;

    private final LanguageRepository languageRepository;

    private final LanguageCreateMapper languageCreateMapper;
    private final LanguageReadMapper languageReadMapper;


    @Transactional
    public Integer create(LanguageCreateDto LanguageCreateDto)
    {
        var validationResult = validator.validate(LanguageCreateDto);
        if (!validationResult.isEmpty())
            throw new ConstraintViolationException(validationResult);

        Language languageEntity = languageCreateMapper.mapFrom(LanguageCreateDto);
        return languageRepository.save(languageEntity).getId();
    }

    @Transactional
    public Optional<LanguageReadDto> findById(Integer id)
    {
        return languageRepository.findById(id).map(languageReadMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Integer id)
    {
        var mayBeLanguage = languageRepository.findById(id);
        mayBeLanguage.ifPresent(film -> languageRepository.delete(film.getId()));

        return mayBeLanguage.isPresent();
    }
}
