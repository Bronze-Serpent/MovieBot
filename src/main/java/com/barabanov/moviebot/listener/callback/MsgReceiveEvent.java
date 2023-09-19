package com.barabanov.moviebot.listener.callback;

import com.barabanov.moviebot.listener.AbstractEvent;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public class MsgReceiveEvent extends AbstractEvent
{
    @Getter
    // вообще мне достаточно только текста и id, но что потребуется в будущем сложно предположить
    // т.к. я не очень хорошо знаю api тг бота.
    // Может, потребуется информация о том пересланное ли это сообщение или
    // на какое сообщение ссылается отправленное пользователем сообщение. Поэтому тут целый Message.
    private final Message msg;


    public MsgReceiveEvent(Message msg, Consumer<BotApiMethodMessage> sendMsgKeeper)
    {
        super(sendMsgKeeper);
        this.msg = msg;
    }
}
