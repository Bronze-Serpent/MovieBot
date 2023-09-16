package com.barabanov.moviebot.repository;


import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;


public class FilmRepository extends BaseRepository<Long, Film>
{

    public FilmRepository(EntityManager entityManager)
    {
        super(entityManager, Film.class);
    }

    public List<Film> findByTitle(String title) { return readFilmsWithRestriction("title", title); }

    public List<Film> readWithCategory(Category category) { return readFilmsWithRestriction("category", category); }

    public List<Film> readWithYear(LocalDate year)
    {
        return readFilmsWithRestriction("releaseYear", year);
    }

    public List<Film> readFilmsWithScore(int score)
    {
        return readFilmsWithRestriction("audienceScore", score);
    }


    private List<Film> readFilmsWithRestriction(String fieldName, Object paramVal)
    {
        TypedQuery<Film> query = getEntityManager().createQuery("FROM Film f WHERE f." + fieldName +" = :valParam", Film.class);
        query.setParameter("valParam", paramVal);

        return query.getResultList();

    }

    public EntityGraph<Film> createGraphWithLanguage()
    {
        var graph = getEntityManager().createEntityGraph(Film.class);
        graph.addAttributeNodes("language");

        return graph;
    }
}
