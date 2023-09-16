package com.barabanov.moviebot.service;

import com.barabanov.moviebot.dto.FilmCreateDto;
import com.barabanov.moviebot.dto.FilmReadDto;
import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.mapper.FilmCreateMapper;
import com.barabanov.moviebot.mapper.FilmReadMapper;
import com.barabanov.moviebot.repository.FilmRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@RequiredArgsConstructor
public class FilmService
{
    private final Validator validator;

    private final FilmReadMapper filmReadMapper;
    private final FilmCreateMapper filmCreateMapper;

    private final FilmRepository filmRepository;


    @Transactional
    public FilmReadDto getRandomMovie()
    {
        List<Category> categories = new LinkedList<>(List.of(Category.values()));
        Collections.shuffle(categories);

        for(Category category : categories)
        {
            List<Film> filmsWithCategory = filmRepository.readWithCategory(category);
            if (!filmsWithCategory.isEmpty())
                return filmReadMapper.mapFrom(
                        filmsWithCategory.get(ThreadLocalRandom.current().nextInt(filmsWithCategory.size()))
                );
        }

        return null;
    }

    @Transactional
    public List<FilmReadDto> findByTitle(String title)
    {
        return filmReadMapper.mapFrom(filmRepository.findByTitle(title));
    }

    @Transactional
    public List<FilmReadDto> getBestFilms(int quantity)
    {
        int score = 10; // score 1 to 10
        List<Film> bestFilms = new LinkedList<>();

        while(bestFilms.size() < quantity || score > 0)
        {
            List<Film> filmsWithScore = filmRepository.readFilmsWithScore(score--);
            fillInTheList(bestFilms, quantity, filmsWithScore);
        }

        return filmReadMapper.mapFrom(bestFilms);
    }

    @Transactional
    public List<FilmReadDto> moviesWithYear(LocalDate year)
    {
        return filmReadMapper.mapFrom(filmRepository.readWithYear(year));
    }

    @Transactional
    public List<FilmReadDto> moviesWithCategory(Category category)
    {
        return filmReadMapper.mapFrom(filmRepository.readWithCategory(category));
    }


    @Transactional
    public Long create(FilmCreateDto filmCreateDto)
    {
        // Проблема при валидации Language в LanguageValidator
        var validationResult = validator.validate(filmCreateDto);
        if (!validationResult.isEmpty())
            throw new ConstraintViolationException(validationResult);

        Film filmEntity = filmCreateMapper.mapFrom(filmCreateDto);
        return filmRepository.save(filmEntity).getId();
    }

    @Transactional
    public Optional<FilmReadDto> findById(Long id)
    {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJakartaHintName(), filmRepository.createGraphWithLanguage()
        );

        return filmRepository.findById(id, properties).map(filmReadMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id)
    {
        var mayBeFilm = filmRepository.findById(id);
        mayBeFilm.ifPresent(film -> filmRepository.delete(film.getId()));

        return mayBeFilm.isPresent();
    }


    public static String describeFilm(FilmReadDto filmReadDto)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(filmReadDto.title()).append("\n\n");
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.valueOf(filmReadDto.releaseYear()));
        sb.append("Year: ").append(cal.get(Calendar.YEAR)).append("\n");
        sb.append("Language: ").append(filmReadDto.languageReadDto().name()).append("\n");
        sb.append("Length: ").append(filmReadDto.length()).append("\n");
        sb.append("Category: ").append(filmReadDto.category().getWriting()).append("\n");
        sb.append("Rating: ").append(filmReadDto.rating().getWriting()).append("\n\n");
        sb.append("Description: ").append(filmReadDto.description()).append("\n");

        return sb.toString();
    }


    private void fillInTheList(List<Film> storage, int storageSize, List<Film> resource)
    {
        int emptyPlaces = storageSize - storage.size();

        for (Film film : resource)
        {
            if (emptyPlaces < 1)
                return;
            storage.add(film);

            emptyPlaces--;
        }
    }
}
