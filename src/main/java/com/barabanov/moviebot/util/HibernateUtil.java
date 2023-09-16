package com.barabanov.moviebot.util;

import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.entity.Language;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;


public class HibernateUtil
{

    private static final SessionFactory sessionFactory;

    static
    {
        sessionFactory = buildSessionFactory();
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void closeSessionFactory()
    {
        sessionFactory.close();
    }


    private static SessionFactory buildSessionFactory()
    {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(Film.class);
        configuration.addAnnotatedClass(Language.class);
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
