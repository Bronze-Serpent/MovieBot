package com.barabanov.moviebot.listener.callback;


import com.barabanov.moviebot.listener.callback.CallbackReceiveEvent;

public interface CallbackReceiveEventListener
{
    void onCallbackReceive(CallbackReceiveEvent event);
}
