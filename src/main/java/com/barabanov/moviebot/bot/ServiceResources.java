package com.barabanov.moviebot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ServiceResources
{
    private final Thread MsgReceiver;
    private final Thread MsgSender;
}
