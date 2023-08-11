package com.barabanov.moviebot.bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


public abstract class TgManageMsgBot extends TelegramLongPollingBot
{
    public abstract BotApiMethodMessage getSendMsg() throws InterruptedException;

    public abstract void putSendMsg(BotApiMethodMessage msg);

    public abstract Update getReceivedUpdate() throws InterruptedException;
}
