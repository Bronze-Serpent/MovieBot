package com.barabanov.moviebot.mapper;


import java.util.List;
import java.util.stream.Collectors;


// по-хорошему это всё можно сделать через MapStruct, но пока так
public interface Mapper<F, T>
{
    T mapFrom(F object);

    default List<T> mapFrom(List<F> objects)
    {
        return objects.stream()
                .map(this::mapFrom)
                .collect(Collectors.toList());
    }
}
