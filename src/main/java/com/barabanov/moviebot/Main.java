package com.barabanov.moviebot;


import com.barabanov.moviebot.bot.BotStarter;
import com.barabanov.moviebot.bot.TgManageMsgBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Main
{

    public static void main(String[] args)
    {
        try {
            TgManageMsgBot bot = BotStarter.createBot();
            BotStarter.startBot(bot);
            BotStarter.startServices(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
