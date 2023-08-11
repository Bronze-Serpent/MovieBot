package com.barabanov.moviebot.ORM;

import java.sql.SQLException;


public interface DAOInterface<T>
{
    T readById(long id) throws SQLException;
}
