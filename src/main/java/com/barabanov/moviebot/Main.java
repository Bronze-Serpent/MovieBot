package com.barabanov.moviebot;


import com.barabanov.moviebot.bot.BotUtils;
import com.barabanov.moviebot.bot.TgManageMsgBot;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

@Slf4j
public class Main
{

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, TelegramApiException
    {
        log.info("Getting started with the application.");
        System.out.println("Бот заводится");
        log.info("Creating resources, launching services.");
        TgManageMsgBot bot = BotUtils.createBot();
        var botSession = BotUtils.startBot(bot);
        var serviceResources = BotUtils.startServices(bot);
        System.out.println("Поехали, всё готово");

        var scanner = new Scanner(System.in);
        System.out.print("Введите что-нибудь для прекращения работы бота: ");
        scanner.nextLine();

        System.out.println("Останавливаемся");
        log.info("Stopping the application. Closing resources and stopping services.");
        botSession.stop();
        BotUtils.terminateServiceExecution(serviceResources);
        log.info("Resources must be closed.");

        System.out.println("Бот остановлен");
        log.info("End of application");
    }
}
