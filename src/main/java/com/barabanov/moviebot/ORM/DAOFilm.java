package com.barabanov.moviebot.ORM;

import com.barabanov.moviebot.entity.Film;

import java.sql.SQLException;
import java.util.List;


public interface DAOFilm extends DAOInterface<Film>
{
    List<Film> readWithTitle(String title) throws SQLException;

    List<Film> readWithCategory(String genreName)  throws SQLException;
}
