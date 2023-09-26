package com.barabanov.moviebot.bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public abstract class TgManageMsgBot extends TelegramLongPollingBot
{
    public abstract BotApiMethodMessage getSendMsg() throws InterruptedException;

    public abstract void putSendMsg(BotApiMethodMessage msg);

    public abstract Update getReceivedUpdate() throws InterruptedException;

    public abstract boolean hasSendMsg();

    public abstract boolean hasReceivedMsg();
}
