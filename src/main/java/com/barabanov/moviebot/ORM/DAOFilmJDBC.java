package com.barabanov.moviebot.ORM;

import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.entity.Language;
import com.barabanov.moviebot.entity.Rating;
import com.barabanov.moviebot.service.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class DAOFilmJDBC implements DAOFilm
{

    @Override
    public List<Film> readWithTitle(String title) throws SQLException
    {
        return readWithStrOption( "title", title);
    }


    @Override
    public List<Film> readWithCategory(String categoryName) throws SQLException
    {
        return readWithStrOption("category", categoryName);
    }


    public List<Film> readWithYear(int year) throws SQLException
    {
        return readWithLngOption("release_year", year);
    }


    @Override
    public List<Film> readFilmsWithScore(int score) throws SQLException
    {
        return readWithLngOption("audience_score", score);
    }


    @Override
    public Film readById(long id) throws SQLException
    {
        List<Film> filmsWithId = readWithLngOption("film_id", id);

        //it is assumed that id(film_id) is PRIMARY KEY. Therefore, the number of films with such id will be 0 or 1
        return filmsWithId.size() == 0 ? null : filmsWithId.get(0);
    }


    private List<Film> readWithLngOption(String optionName, long option) throws SQLException
    {
        try (Connection conn = DBManager.getConnection(); PreparedStatement statement =
                conn.prepareStatement("SELECT * FROM film_useful_inf WHERE " + optionName + " = " + option)) {
            try (ResultSet resultSet = statement.executeQuery())
            {
                List<Film> films = new LinkedList<>();
                while (resultSet.next())
                    films.add(convertToFilm(resultSet));

                return films;
            }
        }
    }


    private List<Film> readWithStrOption(String optionName, String option) throws SQLException
    {
        try (Connection conn = DBManager.getConnection(); PreparedStatement statement =
                conn.prepareStatement("SELECT * FROM film_useful_inf WHERE " + optionName + " = '" + option + "'"))
        {
            try (ResultSet resultSet = statement.executeQuery())
            {
                List<Film> films = new LinkedList<>();
                while (resultSet.next())
                    films.add(convertToFilm(resultSet));

                return films;
            }
        }
    }


    private static Category strToCategory(String str)
    {
        if (str == null)
            return null;
        else
            return Category.fromString(str);
    }


    private static Rating strToRating(String str)
    {
        if (str == null)
            return null;
        else
            return Rating.fromString(str);
    }


    private static Language strToLanguage(String str)
    {
        if (str == null)
            return null;
        else
            return Language.fromString(str);
    }


    private static Film convertToFilm(ResultSet resultSet) throws SQLException
    {
        String[] titleWords = resultSet.getString("title").toLowerCase().split(" ");
        char[] firstWordChars = titleWords[0].toCharArray();
        firstWordChars[0] = Character.toUpperCase(firstWordChars[0]);
        titleWords[0] = String.valueOf(firstWordChars);

        return new Film(resultSet.getInt("film_id"), String.join(" ", titleWords),
                resultSet.getString("description"), resultSet.getDate("release_year"),
                strToLanguage(resultSet.getString("language")), resultSet.getInt("length"),
                strToCategory(resultSet.getString("category")), strToRating(resultSet.getString("rating")));
    }

}
