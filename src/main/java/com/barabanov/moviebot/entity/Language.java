package com.barabanov.moviebot.entity;

public enum Language
{
    ENGLISH("English"),
    ITALIAN("Italian"),
    JAPANESE("Japanese"),
    MANDARIN("Mandarin"),
    FRENCH("French");


    private final String writing;

    public static Language fromString(String category)
    {
        return Language.valueOf(category.toUpperCase());
    }

    Language(String writing)
    {
        this.writing = writing;
    }

    public String getWriting() { return writing; }
}
