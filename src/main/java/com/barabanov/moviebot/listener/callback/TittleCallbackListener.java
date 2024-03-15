package com.barabanov.moviebot.listener.callback;

import com.barabanov.moviebot.service.FilmService;
import com.barabanov.moviebot.util.MsgPropUtil;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@RequiredArgsConstructor
public class TittleCallbackListener implements CallbackReceiveEventListener
{
    // TODO: 26.09.2023
    //  если они используются в разных местах, возможно, имеет смысл сделать отдельный класс с константами,
    //  чтобы в разных классах не менять один и тот же путь к сообщению
    private static final String TOP_MOVIES = "btn.top_movies";
    private static final String NEW_FILMS = "btn.new_films";

    private final FilmService filmService;


    @Override
    public void onCallbackReceive(CallbackReceiveEvent event)
    {
        String withBtnText = event.getCallbackQuery().getMessage().getText();
        if (withBtnText.equals(MsgPropUtil.get(TOP_MOVIES)) ||
                withBtnText.equals(MsgPropUtil.get(NEW_FILMS)))
        {
            String btnTxt = event.getCallbackQuery().getData();

            var filmsWithTittle = filmService.findByTitle(btnTxt);
            if (!filmsWithTittle.isEmpty())
            {
                String chatId = event.getCallbackQuery().getMessage().getChatId().toString();
                filmsWithTittle.stream()
                        .map(FilmService::describeFilm)
                        .map((description) -> new SendMessage(chatId, description))
                        .forEach(sendMsg -> event.getSendMsgKeeper().accept(sendMsg));
            }
        }
    }

}
