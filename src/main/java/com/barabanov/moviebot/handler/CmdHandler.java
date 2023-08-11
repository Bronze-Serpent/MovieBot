package com.barabanov.moviebot.handler;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public interface CmdHandler
{
    void handleCommand(Command cmd, Message msg, Consumer<BotApiMethodMessage> resultKeeper);
}
