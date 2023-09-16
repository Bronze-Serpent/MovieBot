package com.barabanov.moviebot.handler;


import com.barabanov.moviebot.service.FilmService;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public class TxtMsgHandler implements MsgHandler
{
    private final FilmService filmService;


    public TxtMsgHandler(FilmService filmService)
    {
        this.filmService = filmService;
    }


    @Override
    public void handleMessage(Message msg, Consumer<BotApiMethodMessage> resultKeeper)
    {
        String text = msg.getText();
        String chatId = msg.getChatId().toString();

        handleMovieTitle(chatId, text, resultKeeper);
    }


    private void handleMovieTitle(String chatId, String title, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(() ->
                filmService.findByTitle(title).stream()
                        .map(FilmService::describeFilm)
                        .map((description) -> new SendMessage(chatId, description))
                        .forEach(resultKeeper));
        handler.start();
    }


}
