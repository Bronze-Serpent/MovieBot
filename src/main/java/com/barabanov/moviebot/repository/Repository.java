package com.barabanov.moviebot.repository;


import com.barabanov.moviebot.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


public interface Repository<K extends Serializable, E extends BaseEntity<K>>
{
    E save(E entity);

    default Optional<E> findById(K id)
    {
        return findById(id, Collections.emptyMap());
    }

    Optional<E> findById(K id, Map<String, Object> properties);

    void update(E entity);

    void delete(K id);
}
