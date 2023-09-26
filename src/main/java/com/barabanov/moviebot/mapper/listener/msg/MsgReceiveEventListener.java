package com.barabanov.moviebot.mapper.listener.msg;


import com.barabanov.moviebot.mapper.listener.callback.MsgReceiveEvent;

public interface MsgReceiveEventListener
{
    void onMsgReceive(MsgReceiveEvent event);
}
