package com.barabanov.moviebot.service;


import com.barabanov.moviebot.handler.*;
import com.barabanov.moviebot.bot.TgManageMsgBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;


public class MsgReceiver implements Runnable
{

    private final TgManageMsgBot bot;

    private final CmdHandler cmdHandler;
    private final ButtonHandler btnHandler;
    private final MsgHandler msgHandler;
    private final CallBackHandler callBackHandler;


    public MsgReceiver(TgManageMsgBot bot, CmdHandler cmdHandler, ButtonHandler btnHandler, MsgHandler msgHandler, CallBackHandler callBackHandler)
    {
        this.bot = bot;
        this.cmdHandler = cmdHandler;
        this.btnHandler = btnHandler;
        this.msgHandler = msgHandler;
        this.callBackHandler = callBackHandler;
    }


    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Update update = bot.getReceivedUpdate();
                Consumer<BotApiMethodMessage> resultKeeper = bot::putSendMsg;

                if (update.hasMessage())
                {
                    Message msg = update.getMessage();
                    if (msg.hasText())
                    {
                        // обрабатываем команды, домашние статические кнопки и просто сообщения
                        String text = msg.getText().trim();
                        if (Command.isItCmd(text))
                            cmdHandler.handleCommand(Command.fromString(text), msg, resultKeeper);
                        if (Button.isItButtonMsg(text))
                            btnHandler.handleButton(Button.fromString(text), msg, resultKeeper);
                        msgHandler.handleMessage(msg, resultKeeper);
                    }
                }
                else if (update.hasCallbackQuery())
                    callBackHandler.handleCallBack(update.getCallbackQuery(), resultKeeper);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
