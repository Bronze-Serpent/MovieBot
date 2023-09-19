package com.barabanov.moviebot.repository;


import com.barabanov.moviebot.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public abstract class BaseRepository<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E>
{

    @Getter
    private final EntityManager entityManager;

    // можно попробовать убрать это поле, используя рефлексию для получения E.class,
    // но так проще, а работа с Class не выходит выше Repository
    private final Class<E> clazz;


    @Override
    public E save(E entity)
    {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public Optional<E> findById(K id, Map<String, Object> properties)
    {
        return Optional.ofNullable(entityManager.find(clazz, id, properties));
    }

    @Override
    public void update(E entity)
    {
        entityManager.merge(entity);
    }

    @Override
    public void delete(K id)
    {
        entityManager.remove(id);
        entityManager.flush();
    }
}
