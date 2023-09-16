package com.barabanov.moviebot;


import com.barabanov.moviebot.bot.BotStarter;
import com.barabanov.moviebot.bot.TgManageMsgBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;


public class Main
{

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TelegramApiException
    {
        TgManageMsgBot bot = BotStarter.createBot();
        BotStarter.startBot(bot);
        BotStarter.startServices(bot);
    }
}
