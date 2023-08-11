package com.barabanov.moviebot.entity;

import java.util.Date;


public record Film (int filmId, String title, String description, Date releaseYear,
                    Language language, int length, Category category, Rating rating)
    {
        @Override
        public String toString()
        {
            return String.format("Film{filmId=%d, title=%s, description=%s, releaseYear=%s, language=%s, length=%d, category=%s, rating=%s}", filmId, title, description, releaseYear.toString(), language.toString(),
                    length, category.toString(), rating.toString());
        }
    }
