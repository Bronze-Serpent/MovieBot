package com.barabanov.moviebot.mapper.listener.callback;

import com.barabanov.moviebot.mapper.listener.AbstractEvent;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.function.Consumer;


public class CallbackReceiveEvent extends AbstractEvent
{
    @Getter
    private final CallbackQuery callbackQuery;

    public CallbackReceiveEvent(CallbackQuery callbackQuery, Consumer<BotApiMethodMessage> sendMsgKeeper)
    {
        super(sendMsgKeeper);
        this.callbackQuery = callbackQuery;
    }
}
