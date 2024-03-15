package com.barabanov.moviebot.listener.msg;


import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;
import com.barabanov.moviebot.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@RequiredArgsConstructor
public class TittleMsgListener implements MsgReceiveEventListener
{

    private final FilmService filmService;

    @Override
    public void onMsgReceive(MsgReceiveEvent event)
    {
        if (event.getMsg().hasText())
        {
            String msgText = event.getMsg().getText();
            if (!msgText.startsWith("/") && !MenuButton.isItButtonMsg(msgText))
            {
                var filmsWithTittle = filmService.findByTitle(msgText);
                if (!filmsWithTittle.isEmpty())
                {
                    String chatId = event.getMsg().getChatId().toString();
                    filmsWithTittle.stream()
                            .map(FilmService::describeFilm)
                            .map((description) -> new SendMessage(chatId, description))
                            .forEach(sendMsg -> event.getSendMsgKeeper().accept(sendMsg));
                }
            }
        }
    }

}
