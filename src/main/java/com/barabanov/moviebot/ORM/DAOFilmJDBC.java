package com.barabanov.moviebot.ORM;

import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.entity.Language;
import com.barabanov.moviebot.entity.Rating;
import com.barabanov.moviebot.service.DBManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    @Override
    public Film readById(long id) throws SQLException {
        try (PreparedStatement statement = DBManager.getConnection().prepareStatement("""
                    SELECT *
                    FROM film_useful_inf
                    WHERE film_id = ?
                    """)) {
                {
                    statement.setLong(1, id);
                    try (ResultSet resultSet = statement.executeQuery())
                    {
                        if (resultSet.next())
                            return convertToFilm(resultSet);
                        else
                            return null;
                    }
                }
            }
        }


    private List<Film> readWithStrOption(String optionName, String option) throws SQLException
    {
        try (PreparedStatement statement = DBManager.getConnection()
                .prepareStatement("SELECT * FROM film_useful_inf WHERE " + optionName + " = '" + option + "'")) {
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


    private Rating strToRating(String str)
    {
        if (str == null)
            return null;
        else
            return Rating.fromString(str);
    }


    private Language strToLanguage(String str)
    {
        if (str == null)
            return null;
        else
            return Language.fromString(str);
    }


    private Film convertToFilm(ResultSet resultSet) throws SQLException
    {
        return new Film(resultSet.getInt("film_id"), resultSet.getString("title"),
                resultSet.getString("description"), resultSet.getDate("release_year"),
                strToLanguage(resultSet.getString("language")), resultSet.getInt("length"),
                strToCategory(resultSet.getString("category")), strToRating(resultSet.getString("rating")));
    }

}
