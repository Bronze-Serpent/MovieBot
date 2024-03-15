package com.barabanov.moviebot.listener;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.stream.Stream;


@UtilityClass
public class ResponseGenerationUtil
{
    public static SendMessage createInlineBtnSendMsg(Stream<String> btnNames, String msgText)
    {
        List<List<InlineKeyboardButton>> buttons =  btnNames.map((title) ->
                {
                    InlineKeyboardButton btn = new InlineKeyboardButton();
                    btn.setText(title);
                    btn.setCallbackData(title);
                    return  btn;
                })
                .map(List::of)
                .toList();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(buttons));
        sendMessage.setText(msgText);

        return sendMessage;
    }
}
