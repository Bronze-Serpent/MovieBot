package com.barabanov.moviebot.listener.msg;


import com.barabanov.moviebot.listener.callback.MsgReceiveEvent;
import com.barabanov.moviebot.util.MsgPropUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class CmdMsgListener implements MsgReceiveEventListener
{

    private final static String START = "cmd.start";
    private final static String HELP = "cmd.help";


    @Override
    public void onMsgReceive(MsgReceiveEvent event)
    {
        if (event.getMsg().hasText())
        {
            String msgText = event.getMsg().getText();
            if (Command.isItCmd(msgText))
                event.getSendMsgKeeper().accept(
                        handleCommand(
                                Command.fromString(msgText),
                                event.getMsg().getChatId().toString())
                );
        }
    }


    private SendMessage handleCommand(Command cmd, String chatId)
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
                SendMessage buttonMsg = new SendMessage(chatId, MsgPropUtil.get(START));
                buttonMsg.setReplyMarkup(keyboardMarkup);
                return buttonMsg;
            }
            case HELP -> {
                return new SendMessage(chatId, MsgPropUtil.get(HELP));
            }
        }

        // unreachable string if all commands were listed in the switch
        return null;
    }

}
