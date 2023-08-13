package com.barabanov.moviebot.handler;

import java.util.Arrays;

public enum Button
{
    CATEGORIES("Categories"),
    TOP_MOVIES("Top movies"),
    NEW_FILMS("New films"),
    RANDOM_MOVIE("Random movie");


    private final String msgSyntax;


    Button(String callBackSyntax)
    {
        this.msgSyntax = callBackSyntax;
    }


    public static Button fromString(String callBackAsTxt)
    {
        return Button.valueOf(String.join("_", callBackAsTxt.split(" ")).toUpperCase());
    }


    public static boolean isItButtonMsg(String callBackAsTxt)
    {
        for (String callBack : Arrays.stream(Button.values()).map(Button::getMsgSyntax).toList())
            if (callBackAsTxt.equals(callBack))
                return true;

        return false;
    }

    public String getMsgSyntax() { return msgSyntax; }
}
