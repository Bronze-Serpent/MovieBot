package com.barabanov.moviebot.handler;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public class HomeBtnHandler implements ButtonHandler
{

    @Override
    public void handleButton(Button btn, Message msg, Consumer<BotApiMethodMessage> resultKeeper)
    {
        String chatId = String.valueOf(msg.getChatId());

        switch (btn)
        {
            case GENRES, TOP_MOVIES, NEW_FILMS, RANDOM_MOVIE:
                resultKeeper.accept(new SendMessage(chatId, "Haven't done yet"));

        }
    }
}
