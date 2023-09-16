package com.barabanov.moviebot.repository;

import com.barabanov.moviebot.entity.Language;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;


public class LanguageRepository extends BaseRepository<Integer, Language>
{
    public LanguageRepository(EntityManager entityManager)
    {
        super(entityManager, Language.class);
    }


    public Optional<Language> findByName(String languageName)
    {
        try
        {
            Language language = getEntityManager().createQuery("select l from Language l where l.name = :languageName", Language.class)
                    .setParameter("languageName", languageName)
                    .getSingleResult();

            return Optional.of(language);
        }
        catch (NoResultException e)
        {
            return Optional.empty();
        }
    }
}
