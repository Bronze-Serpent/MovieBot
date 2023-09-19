package com.barabanov.moviebot.listener.msg;

import java.util.Arrays;


public enum MenuButton
{
    CATEGORIES("Categories"),
    TOP_MOVIES("Top movies"),
    NEW_FILMS("New films"),
    RANDOM_MOVIE("Random movie");


    private final String msgSyntax;


    MenuButton(String callBackSyntax)
    {
        this.msgSyntax = callBackSyntax;
    }


    public static MenuButton fromString(String callBackAsTxt)
    {
        return MenuButton.valueOf(String.join("_", callBackAsTxt.split(" ")).toUpperCase());
    }


    public static boolean isItButtonMsg(String callBackAsTxt)
    {
        for (String callBack : Arrays.stream(MenuButton.values()).map(MenuButton::getMsgSyntax).toList())
            if (callBackAsTxt.equals(callBack))
                return true;

        return false;
    }

    public String getMsgSyntax() { return msgSyntax; }
}
