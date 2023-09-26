package com.barabanov.moviebot.service.msg.sevice;


import com.barabanov.moviebot.bot.TgManageMsgBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@RequiredArgsConstructor
public class MsgSender implements Runnable
{
    private final TgManageMsgBot bot;


    @Override
    public void run()
    {
        log.info("MsgSender started to work");
        while (! Thread.currentThread().isInterrupted())
        {
            try
            {
                if (bot.hasSendMsg())
                {
                    try
                    {
                        BotApiMethodMessage msg = bot.getSendMsg();
                        bot.execute(msg);
                    } catch (InterruptedException e)
                    {
                        log.error("An InterruptedException upon attempt bot.getSendMsg()", e);
                        throw new RuntimeException(e);
                    }
                }
            } catch (TelegramApiException e)
            {
                log.error("An TelegramApiException", e);
                throw new RuntimeException(e);
            }
        }
        log.info("MsgSender finished work");
    }

}
