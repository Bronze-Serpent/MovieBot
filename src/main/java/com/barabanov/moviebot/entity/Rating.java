package com.barabanov.moviebot.entity;

import java.util.Arrays;

public enum Rating
{
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");


    private final String writing;

    public static Rating fromString(String category)
    {
        return Rating.valueOf(String.join("_", category.split("-")).toUpperCase());
    }

    Rating(String writing)
    {
        this.writing = writing;
    }

    public static boolean isItRating(String line)
    {
        return Arrays.stream(Rating.values())
                .map(Rating::getWriting)
                .anyMatch(ratingWriting -> ratingWriting.equals(line));
    }

    public String getWriting() { return writing; }
}
