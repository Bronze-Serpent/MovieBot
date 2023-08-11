package com.barabanov.moviebot.handler;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public interface MsgHandler
{
    void handleMessage(Message msg, Consumer<BotApiMethodMessage> resultKeeper);
}
