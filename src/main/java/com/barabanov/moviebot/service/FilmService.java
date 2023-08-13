package com.barabanov.moviebot.service;

import com.barabanov.moviebot.ORM.DAOFilm;
import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class FilmService
{

    private final DAOFilm daoFilm;


    public FilmService(DAOFilm daoFilm)
    {
        this.daoFilm = daoFilm;
    }


    public Film getRandomMovie()
    {
        try
        {
            List<Category> categories = Arrays.stream(Category.values()).toList();
            List<Film> filmsWithCat = new LinkedList<>();
            while (filmsWithCat.size() == 0)
                filmsWithCat = daoFilm.readWithCategory(categories.get(ThreadLocalRandom.current().nextInt(categories.size())).getWriting());

            return filmsWithCat.get(ThreadLocalRandom.current().nextInt(filmsWithCat.size()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Film> findMovie(String title)
    {
        try {
            return daoFilm.readWithTitle(title.toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Film> getBestFilms(int quantity)
    {
        int score = 10; // score 1 to 10
        List<Film> bestFilms = new LinkedList<>();

        while(bestFilms.size() < quantity || score > 1)
        {
            try {
                List<Film> filmsWithScore = daoFilm.readFilmsWithScore(score--);
                fillInTheList(bestFilms, quantity, filmsWithScore);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return bestFilms;
    }


    public List<Film> moviesWithYear(int year)
    {
        try
        {
            return daoFilm.readWithYear(year);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Film> moviesWithCategory(Category category)
    {
        try
        {
            return daoFilm.readWithCategory(category.getWriting());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static String describeFilm(Film film)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(film.title()).append("\n\n");
        Calendar cal = Calendar.getInstance();
        cal.setTime(film.releaseYear());
        sb.append("Year: ").append(cal.get(Calendar.YEAR)).append("\n");
        sb.append("Language: ").append(film.language().getWriting()).append("\n");
        sb.append("Length: ").append(film.length()).append("\n");
        sb.append("Category: ").append(film.category().getWriting()).append("\n");
        sb.append("Rating: ").append(film.rating().getWriting()).append("\n\n");
        sb.append("Description: ").append(film.description()).append("\n");

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
