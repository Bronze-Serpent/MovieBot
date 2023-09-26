package com.barabanov.moviebot.mapper.listener.msg;

import java.util.Arrays;

public enum Command
{
    START("/start", "displays a greeting, talks about himself and talks about help"),
    HELP("/help", "describe the capabilities of the bot");


    private final String cmdSyntax;
    private final String description;


    Command(String cmdSyntax, String description)
    {
        this.cmdSyntax = cmdSyntax;
        this.description = description;
    }


    public static Command fromString(String cmdAsTxt)
    {
        return Command.valueOf(cmdAsTxt.substring(1).toUpperCase());
    }

    public static boolean isItCmd(String cmdAsTxt)
    {
        for (String cmd : Arrays.stream(Command.values()).map(Command::getCmdSyntax).toList())
            if (cmdAsTxt.equals(cmd))
                return true;

        return false;
    }


    public String getCmdSyntax() { return cmdSyntax; }

    public String getDescription() { return description; }
}