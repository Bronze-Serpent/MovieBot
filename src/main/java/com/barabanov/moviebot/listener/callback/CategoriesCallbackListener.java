package com.barabanov.moviebot.listener.callback;

import com.barabanov.moviebot.dto.FilmReadDto;
import com.barabanov.moviebot.entity.Category;
import com.barabanov.moviebot.listener.ResponseGenerationUtil;
import com.barabanov.moviebot.service.FilmService;
import com.barabanov.moviebot.util.MsgPropUtil;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@RequiredArgsConstructor
public class CategoriesCallbackListener implements CallbackReceiveEventListener
{

    private static final int FILMS_QUANTITY = 15;
    private static final String CATEGORIES = "btn.categories";

    private final FilmService filmService;

    @Override
    public void onCallbackReceive(CallbackReceiveEvent event)
    {
        String withBtnText = event.getCallbackQuery().getMessage().getText();
        if (withBtnText.equals(MsgPropUtil.get(CATEGORIES)))
        {
            String btnTxt= event.getCallbackQuery().getData();
            event.getSendMsgKeeper().accept(handleCategoriesCallBack(
                    event.getCallbackQuery().getMessage().getChatId().toString(),
                    Category.fromString(btnTxt)
            ));
        }

    }


    private SendMessage handleCategoriesCallBack(String chatId, Category category)
    {
        var sendMsg = ResponseGenerationUtil.createInlineBtnSendMsg(filmService.moviesWithCategory(category).stream()
                .limit(FILMS_QUANTITY)
                .map(FilmReadDto::title),
                "Movies in the category " + category.getWriting() + ":");
        sendMsg.setChatId(chatId);

        return sendMsg;
    }
}
