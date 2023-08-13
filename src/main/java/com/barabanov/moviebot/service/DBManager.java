package com.barabanov.moviebot.service;


import com.barabanov.moviebot.util.AppPropUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DBManager
{

    private static final Properties PROPERTIES = new Properties();

    private static final HikariDataSource ds;


    static
    {
        loadProperties();

        HikariConfig config = new HikariConfig(PROPERTIES);

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void closeConnections()
    {
        ds.close();
    }


    private static void loadProperties()
    {
        try (var inputStream = AppPropUtil.class.getClassLoader().getResourceAsStream("database.properties"))
        {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
