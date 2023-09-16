package com.barabanov.moviebot.service.msg.sevice;


import com.barabanov.moviebot.bot.TgManageMsgBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class MsgSender implements Runnable
{

    private final TgManageMsgBot bot;


    public MsgSender(TgManageMsgBot bot) {
        this.bot = bot;
    }


    @Override
    public void run()
    {
        while (true)
        {
            try {
                BotApiMethodMessage msg = bot.getSendMsg();
                bot.execute(msg);
            }
            catch (TelegramApiException e)
            {
                System.out.println("An TelegramApiException" + e);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
