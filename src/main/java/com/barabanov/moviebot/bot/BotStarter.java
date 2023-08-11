package com.barabanov.moviebot.bot;


import com.barabanov.moviebot.handler.Command;
import com.barabanov.moviebot.handler.HomeBtnHandler;
import com.barabanov.moviebot.handler.SimpleCmdHandler;
import com.barabanov.moviebot.handler.TxtMsgHandler;
import com.barabanov.moviebot.service.MsgReceiver;
import com.barabanov.moviebot.service.MsgSender;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;


public class BotStarter
{

    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;


    public static void startBot(TgManageMsgBot bot)
    {
        try
        {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public static void startServices(TgManageMsgBot bot)
    {
        // Daemon потоки можно будет заменить обычными с interrupt(),
        // но для этого нужно будет сделать консольное приложение из проекта. Чтобы где-то вводить stop,
        // оповещать потоки о прекращении работы и они завершали своё выполнение
        MsgReceiver msgReceiver = new MsgReceiver(bot, new SimpleCmdHandler(),
                new HomeBtnHandler(), new TxtMsgHandler());
        Thread receiver = new Thread(msgReceiver);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        MsgSender msgSender = new MsgSender(bot);
        Thread sender = new Thread(msgSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();
    }


    public static TgManageMsgBot createBot() throws TelegramApiException
    {
        TgManageMsgBot bot = new MovieBotTG();

        SetMyCommands commandsSet = SetMyCommands
                .builder()
                .commands(Arrays.stream(Command.values())
                        .map(cmd -> new BotCommand(cmd.getCmdSyntax(), cmd.getDescription()))
                        .toList())
                .build();
        bot.executeAsync(commandsSet);

        return bot;
    }
}
