package com.barabanov.moviebot.service.msg.sevice;


import com.barabanov.moviebot.bot.TgManageMsgBot;
import com.barabanov.moviebot.listener.callback.CallbackReceiveEvent;
import com.barabanov.moviebot.listener.callback.CallbackReceiveEventListener;
import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;
import com.barabanov.moviebot.listener.msg.MsgReceiveEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class MsgReceiver implements Runnable
{

    private final TgManageMsgBot bot;

    private final List<MsgReceiveEventListener> msgListeners = new ArrayList<>();

    private final List<CallbackReceiveEventListener> callbackListeners = new ArrayList<>();


    @Override
    public void run()
    {
        log.info("MsgReceiver started to work");
        while (! Thread.currentThread().isInterrupted())
        {
            if (bot.hasReceivedMsg())
            {
                try
                {
                    // При таком подходе может стать очень много потоков. Нужен какой-нибудь пул потоков.
                    Thread updateHandlerThread = new Thread(new UpdateHandler(bot.getReceivedUpdate(), bot::putSendMsg));
                    updateHandlerThread.start();

                } catch (InterruptedException e)
                {
                    log.error("An InterruptedException upon attempt bot.getReceivedUpdate()", e);
                    throw new RuntimeException(e);
                }
            }
        }
        log.info("MsgReceiver finished work");
    }


    public void addCallbackListener(CallbackReceiveEventListener listener)
    {
        callbackListeners.add(listener);
    }

    public void addMsgListener(MsgReceiveEventListener listener)
    {
        msgListeners.add(listener);
    }


    @RequiredArgsConstructor
    private class UpdateHandler implements Runnable
    {
        private final Update update;
        private final Consumer<BotApiMethodMessage> resultKeeper;

        @Override
        public void run() {
            if (update.hasMessage())
            {
                var msgReceiveEvent = new MsgReceiveEvent(update.getMessage(), resultKeeper);
                for (MsgReceiveEventListener msgListener : msgListeners)
                    msgListener.onMsgReceive(msgReceiveEvent);
            }
            else if (update.hasCallbackQuery())
            {
                var callbackReceiveEvent = new CallbackReceiveEvent(update.getCallbackQuery(), resultKeeper);
                for (CallbackReceiveEventListener callbackListener : callbackListeners)
                    callbackListener.onCallbackReceive(callbackReceiveEvent);
            }
        }
    }
}
