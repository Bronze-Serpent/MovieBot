package com.barabanov.moviebot.handler;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public interface ButtonHandler
{
    void handleButton(Button btn, Message msg, Consumer<BotApiMethodMessage> resultKeeper);
}
