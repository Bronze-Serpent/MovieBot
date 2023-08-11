package com.barabanov.moviebot.handler;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;


public class TxtMsgHandler implements MsgHandler
{

    @Override
    public void handleMessage(Message msg, Consumer<BotApiMethodMessage> resultKeeper)
    {

    }
}
