package com.barabanov.moviebot.util;

import java.io.IOException;
import java.util.Properties;


public class MsgPropUtil
{

    private static final Properties PROPERTIES = new Properties();


    static
    {
        loadProperties();
    }


    private MsgPropUtil() {}


    public static String get(String key)
    {
        return PROPERTIES.getProperty(key);
    }


    private static void loadProperties()
    {
        try (var inputStream = AppPropUtil.class.getClassLoader().getResourceAsStream("messages.properties"))
        {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
