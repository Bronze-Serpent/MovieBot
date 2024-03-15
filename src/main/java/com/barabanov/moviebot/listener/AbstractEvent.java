package com.barabanov.moviebot.listener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;

import java.util.function.Consumer;


@RequiredArgsConstructor
public abstract class AbstractEvent
{
    @Getter
    private final Consumer<BotApiMethodMessage> sendMsgKeeper;
}
