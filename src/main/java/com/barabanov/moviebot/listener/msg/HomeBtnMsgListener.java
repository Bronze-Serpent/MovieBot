package com.barabanov.moviebot.listener.msg;

import com.barabanov.moviebot.dto.FilmReadDto;
import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;
import com.barabanov.moviebot.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.Arrays;

import static com.barabanov.moviebot.listener.ResponseGenerationUtil.createInlineBtnSendMsg;


@RequiredArgsConstructor
public class HomeBtnMsgListener implements MsgReceiveEventListener
{
    public static final String CATEGORIES_MSG = "Categories:";
    public static final String TOP_MOVIES_MSG = "Top Movies:";
    public static final String NEW_FILMS_MSG = "Some last movies:";

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
                sendMessage = createInlineBtnSendMsg(
                        Arrays.stream(Category.values())
                                .map(Category::getWriting),
                        CATEGORIES_MSG
                );
            case RANDOM_MOVIE -> {
                sendMessage = new SendMessage();
                sendMessage.setText(FilmService.describeFilm(filmService.getRandomMovie()));
            }
            case NEW_FILMS ->
                sendMessage = createInlineBtnSendMsg(
                        filmService.moviesWithYear(LocalDate.now()).stream()
                                .limit(NEW_FILMS_QUANTITY)
                                .map(FilmReadDto::title),
                        NEW_FILMS_MSG
                );
            case TOP_MOVIES ->
                sendMessage = createInlineBtnSendMsg(
                        filmService.getBestFilms(BEST_FILMS_QUANTITY).stream()
                                .map(FilmReadDto::title),
                        TOP_MOVIES_MSG
                );
        }
        sendMessage.setChatId(chatId);

        return sendMessage;
    }
}
