package com.barabanov.moviebot.service.msg.sevice;


import com.barabanov.moviebot.bot.TgManageMsgBot;
import com.barabanov.moviebot.listener.callback.CallbackReceiveEvent;
import com.barabanov.moviebot.listener.callback.CallbackReceiveEventListener;
import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;
import com.barabanov.moviebot.listener.msg.MsgReceiveEventListener;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@RequiredArgsConstructor
public class MsgReceiver implements Runnable
{

    private final TgManageMsgBot bot;

    private final List<MsgReceiveEventListener> msgListeners = new ArrayList<>();

    private final List<CallbackReceiveEventListener> callbackListeners = new ArrayList<>();


    @Override
    public void run()
    {
        @RequiredArgsConstructor
        class UpdateHandler implements Runnable
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

        while (true)
        {
            try
            {
                // При таком подходе может стать очень много потоков. Нужен какой-нибудь пул потоков.
                var updateHandlerThread = new Thread(new UpdateHandler(bot.getReceivedUpdate(), bot::putSendMsg));
                updateHandlerThread.start();
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

        }

    }


    public void addCallbackListener(CallbackReceiveEventListener listener)
    {
        callbackListeners.add(listener);
    }

    public void addMsgListener(MsgReceiveEventListener listener)
    {
        msgListeners.add(listener);
    }
}
