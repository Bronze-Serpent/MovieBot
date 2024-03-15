package com.barabanov.moviebot.listener.msg;


import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;

public interface MsgReceiveEventListener
{
    void onMsgReceive(MsgReceiveEvent event);
}
