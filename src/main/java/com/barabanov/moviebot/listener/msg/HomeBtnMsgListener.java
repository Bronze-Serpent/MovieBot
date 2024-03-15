package com.barabanov.moviebot.listener.msg;

import com.barabanov.moviebot.dto.FilmReadDto;
import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;
import com.barabanov.moviebot.listener.ResponseGenerationUtil;
import com.barabanov.moviebot.service.FilmService;
import com.barabanov.moviebot.util.MsgPropUtil;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Arrays;


@RequiredArgsConstructor
public class HomeBtnMsgListener implements MsgReceiveEventListener
{
    private static final String CATEGORIES = "btn.categories";
    private static final String TOP_MOVIES = "btn.top_movies";
    private static final String NEW_FILMS = "btn.new_films";

    private static final int NEW_FILMS_QUANTITY = 15;
    private static final int BEST_FILMS_QUANTITY = 10;

    private final FilmService filmService;


    @Override
    public void onMsgReceive(MsgReceiveEvent event)
    {
        if (event.getMsg().hasText())
        {
            String msgText = event.getMsg().getText();
            if (MenuButton.isItButtonMsg(msgText))
                event.getSendMsgKeeper().accept(
                        handleButton(
                                MenuButton.fromString(msgText),
                                event.getMsg().getChatId().toString())
                );
        }
    }


    private SendMessage handleButton(MenuButton btn, String chatId)
    {
        SendMessage sendMessage = null;
        switch (btn) {
            case CATEGORIES ->
                sendMessage = ResponseGenerationUtil.createInlineBtnSendMsg(
                        Arrays.stream(Category.values())
                                .map(Category::getWriting),
                        MsgPropUtil.get(CATEGORIES)
                );
            case RANDOM_MOVIE -> {
                sendMessage = new SendMessage();
                sendMessage.setText(FilmService.describeFilm(filmService.getRandomMovie()));
            }
            case NEW_FILMS ->
                sendMessage = ResponseGenerationUtil.createInlineBtnSendMsg(
                        filmService.moviesWithYear(LocalDate.now()).stream()
                                .limit(NEW_FILMS_QUANTITY)
                                .map(FilmReadDto::title),
                        MsgPropUtil.get(NEW_FILMS)
                );
            case TOP_MOVIES ->
                sendMessage = ResponseGenerationUtil.createInlineBtnSendMsg(
                        filmService.getBestFilms(BEST_FILMS_QUANTITY).stream()
                                .map(FilmReadDto::title),
                        MsgPropUtil.get(TOP_MOVIES)
                );
        }
        sendMessage.setChatId(chatId);

        return sendMessage;
    }
}
