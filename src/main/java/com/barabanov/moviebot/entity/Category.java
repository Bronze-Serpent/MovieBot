package com.barabanov.moviebot.entity;


public enum Category
{
    ACTION("Action"),
    ANIMATION("Animation"),
    CHILDREN("Children"),
    CLASSICS("Classics"),
    COMEDY("Comedy"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FOREIGN("Foreign"),
    GAMES("Games"),
    HORROR("Horror"),
    MUSIC("Music"),
    NEW("new"),
    SCI_FI("Sci-Fi"),
    SPORTS("Sports"),
    TRAVEL("Travel");


    private final String writing;


    Category(String writing)
    {
        this.writing = writing;
    }


    public static Category fromString(String category)
    {
        return Category.valueOf(String.join("_", category.split("-")).toUpperCase());
    }


    public String getWriting() { return writing; }
}
