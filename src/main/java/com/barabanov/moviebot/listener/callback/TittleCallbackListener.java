package com.barabanov.moviebot.listener.callback;

import com.barabanov.moviebot.listener.msg.HomeBtnMsgListener;
import com.barabanov.moviebot.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@RequiredArgsConstructor
public class TittleCallbackListener implements CallbackReceiveEventListener
{

    private final FilmService filmService;


    @Override
    public void onCallbackReceive(CallbackReceiveEvent event)
    {
        String withBtnText = event.getCallbackQuery().getMessage().getText();
        if (withBtnText.equals(HomeBtnMsgListener.TOP_MOVIES_MSG) ||
                withBtnText.equals(HomeBtnMsgListener.NEW_FILMS_MSG))
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
