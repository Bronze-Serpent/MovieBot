package com.barabanov.moviebot.handler;


import com.barabanov.moviebot.util.MsgPropUtil;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
import java.util.function.Consumer;


public class SimpleCmdHandler implements CmdHandler
{

    private final static String START = "msg.start";
    private final static String HELP = "msg.help";

    @Override
    public void handleCommand(Command cmd, Message msg, Consumer<BotApiMethodMessage> resultKeeper)
    {
        switch (cmd) {
            case START ->
            {
                KeyboardButton genresBtn = new KeyboardButton(MenuButton.CATEGORIES.getMsgSyntax());
                KeyboardButton topMoviesBtn = new KeyboardButton(MenuButton.TOP_MOVIES.getMsgSyntax());
                KeyboardButton newFilmsBtn = new KeyboardButton(MenuButton.NEW_FILMS.getMsgSyntax());
                KeyboardButton rndMovieBtn = new KeyboardButton(MenuButton.RANDOM_MOVIE.getMsgSyntax());

                KeyboardRow fstBtnRow = new KeyboardRow();
                fstBtnRow.addAll(List.of(genresBtn, topMoviesBtn));
                KeyboardRow scdBtnRow = new KeyboardRow();
                scdBtnRow.addAll(List.of(newFilmsBtn, rndMovieBtn));

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setResizeKeyboard(true);
                keyboardMarkup.setSelective(true);
                keyboardMarkup.setKeyboard(List.of(fstBtnRow, scdBtnRow));
                SendMessage buttonMsg = new SendMessage(msg.getChatId().toString(), MsgPropUtil.get(START));
                buttonMsg.setReplyMarkup(keyboardMarkup);
                resultKeeper.accept(buttonMsg);
            }
            case HELP -> resultKeeper.accept(new SendMessage(msg.getChatId().toString(), MsgPropUtil.get(HELP)));
        }

    }
}
