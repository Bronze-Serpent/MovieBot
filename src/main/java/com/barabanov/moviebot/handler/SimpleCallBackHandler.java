package com.barabanov.moviebot.handler;

import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.service.FilmService;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.function.Consumer;


public class SimpleCallBackHandler implements CallBackHandler
{

    private final FilmService filmService;


    public SimpleCallBackHandler(FilmService filmService)
    {
        this.filmService = filmService;
    }


    @Override
    public void handleCallBack(CallbackQuery callbackQuery, Consumer<BotApiMethodMessage> resultKeeper)
    {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String callBackTxt = callbackQuery.getData();

        String callBackMsgTxt = callbackQuery.getMessage().getText();
        if (callBackMsgTxt.equals(HomeBtnHandler.CATEGORIES_MSG))
            handleCategoriesCallBack(chatId, Category.fromString(callBackTxt), resultKeeper);
        else //otherwise callBack is the title of the movie
            handleMovieTitle(chatId, callBackTxt, resultKeeper);
    }


    private void handleCategoriesCallBack(String chatId, Category category, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(() ->
        {
            int quantityFilms = 15;

            List<List<InlineKeyboardButton>> buttons = filmService.moviesWithCategory(category).stream()
                    .limit(quantityFilms)
                    .map(Film::title)
                    .map((title) -> {
                        InlineKeyboardButton btn = new InlineKeyboardButton();
                        btn.setText(title);
                        btn.setCallbackData(title);
                        return  btn;
                    })
                    .map(List::of)
                    .toList();

            SendMessage sendMessage = new SendMessage(chatId, "Movies in the category " + category.getWriting() + ":");
            sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
            resultKeeper.accept(sendMessage);
        });
        handler.start();
    }


    private void handleMovieTitle(String chatId, String title, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(() ->
                filmService.findMovie(title).stream()
                        .map(FilmService::describeFilm)
                        .map((description) -> new SendMessage(chatId, description))
                        .forEach(resultKeeper));
        handler.start();
    }
}
