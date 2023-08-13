package com.barabanov.moviebot.handler;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.function.Consumer;

public interface CallBackHandler
{
    void handleCallBack(CallbackQuery callbackQuery, Consumer<BotApiMethodMessage> resultKeeper);
}
