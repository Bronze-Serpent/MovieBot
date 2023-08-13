package com.barabanov.moviebot.handler;


import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.entity.Film;
import com.barabanov.moviebot.service.FilmService;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class HomeBtnHandler implements ButtonHandler
{

    public static final String CATEGORIES_MSG = "Categories:";
    public static final String TOP_MOVIES_MSG = "Top Movies:";
    public static final String NEW_FILMS_MSG = "Some last movies:";

    private final FilmService filmService;


    public HomeBtnHandler(FilmService filmService)
    {
        this.filmService = filmService;
    }


    @Override
    public void handleButton(Button btn, Message msg, Consumer<BotApiMethodMessage> resultKeeper)
    {
        String chatId = String.valueOf(msg.getChatId());

        switch (btn) {
            case CATEGORIES -> handleGenres(chatId, resultKeeper);
            case RANDOM_MOVIE -> handleRndMovie(chatId, resultKeeper);
            case NEW_FILMS -> handleNewFilms(chatId, resultKeeper);
            case TOP_MOVIES -> handleTopMovies(chatId, resultKeeper);
        }
    }


    private void handleGenres(String chatId, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(() ->
        {
            List<List<InlineKeyboardButton>> inlineBtnCat = Arrays.stream(Category.values())
                    .map((category) -> {
                        InlineKeyboardButton btn = new InlineKeyboardButton();
                        btn.setText(category.getWriting());
                        btn.setCallbackData(category.getWriting());
                        return btn;
                    })
                    .map(List::of)
                    .toList();

            SendMessage sendMessage = new SendMessage(chatId, CATEGORIES_MSG);
            sendMessage.setReplyMarkup(new InlineKeyboardMarkup(inlineBtnCat));
            resultKeeper.accept(sendMessage);
        });
        handler.start();
    }


    private void handleTopMovies(String chatId, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(() ->
        {
            int bestFilmsQuantity = 10;

            List<List<InlineKeyboardButton>> buttons = filmService.getBestFilms(bestFilmsQuantity).stream()
                    .map(Film::title)
                    .map((title) -> {
                        InlineKeyboardButton btn = new InlineKeyboardButton();
                        btn.setText(title);
                        btn.setCallbackData(title);
                        return  btn;
                    })
                    .map(List::of)
                    .toList();

            SendMessage sendMessage = new SendMessage(chatId, TOP_MOVIES_MSG);
            sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
            resultKeeper.accept(sendMessage);
        });
        handler.start();
    }


    private void handleRndMovie(String chatId, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(
                () -> resultKeeper.accept(new SendMessage(chatId, FilmService.describeFilm(filmService.getRandomMovie()))));
        handler.start();
    }


    private void handleNewFilms(String chatId, Consumer<BotApiMethodMessage> resultKeeper)
    {
        Thread handler = new Thread(() ->
        {
            int newFilmsQuantity = 15;

            List<List<InlineKeyboardButton>> buttons = filmService.moviesWithYear(LocalDate.now().getYear()).stream()
                    .limit(newFilmsQuantity)
                    .map(Film::title)
                    .map((title) -> {
                        InlineKeyboardButton btn = new InlineKeyboardButton();
                        btn.setText(title);
                        btn.setCallbackData(title);
                        return  btn;
                    })
                    .map(List::of)
                    .toList();
            SendMessage sendMessage = new SendMessage(chatId, NEW_FILMS_MSG);
            sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
            resultKeeper.accept(sendMessage);
        });
        handler.start();
    }
}
