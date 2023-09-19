package com.barabanov.moviebot.bot;

import com.barabanov.moviebot.util.AppPropUtil;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TgMovieBot extends TgManageMsgBot
{

    private static final String BOT_TOKEN = "bot.token";
    private static final String BOT_USERNAME = "bot.username";
    private static final String MAX_SIMULATION_MSG = "bot.max.simultaneous.msg";

    private final BlockingQueue<BotApiMethodMessage> SEND_QUEUE = new  LinkedBlockingQueue<>();
    private final BlockingQueue<Update> RECEIVED_QUEUE = new LinkedBlockingQueue<>(Integer.parseInt(AppPropUtil.get(MAX_SIMULATION_MSG)));


    @Override
    public void onUpdateReceived(Update update)
    {
        RECEIVED_QUEUE.add(update);
    }


    @Override
    public String getBotUsername() {
        return AppPropUtil.get(BOT_USERNAME);
    }


    @Override
    public String getBotToken()
    {
        return AppPropUtil.get(BOT_TOKEN);
    }


    @Override
    public BotApiMethodMessage getSendMsg() throws InterruptedException { return SEND_QUEUE.take(); }

    @Override
    public void putSendMsg(BotApiMethodMessage msg)
    {
        SEND_QUEUE.add(msg);
    }

    @Override
    public Update getReceivedUpdate() throws InterruptedException { return RECEIVED_QUEUE.take(); }

}
