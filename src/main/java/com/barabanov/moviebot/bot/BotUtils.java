package com.barabanov.moviebot.bot;


import com.barabanov.moviebot.interseptor.TransactionInterceptor;
import com.barabanov.moviebot.listener.callback.CategoriesCallbackListener;
import com.barabanov.moviebot.listener.callback.TittleCallbackListener;
import com.barabanov.moviebot.listener.msg.CmdMsgListener;
import com.barabanov.moviebot.listener.msg.Command;
import com.barabanov.moviebot.listener.msg.HomeBtnMsgListener;
import com.barabanov.moviebot.listener.msg.TittleMsgListener;
import com.barabanov.moviebot.mapper.FilmCreateMapper;
import com.barabanov.moviebot.mapper.FilmReadMapper;
import com.barabanov.moviebot.mapper.LanguageReadMapper;
import com.barabanov.moviebot.repository.FilmRepository;
import com.barabanov.moviebot.repository.LanguageRepository;
import com.barabanov.moviebot.service.FilmService;
import com.barabanov.moviebot.service.msg.sevice.MsgReceiver;
import com.barabanov.moviebot.service.msg.sevice.MsgSender;
import com.barabanov.moviebot.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;


@Slf4j
public class BotUtils
{

    public static BotSession startBot(TgManageMsgBot bot)
    {
        try
        {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            return telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e)
        {
            log.error("An TelegramApiException when registering a bot", e);
            throw new RuntimeException(e);
        }
    }


    public static ServiceResources startServices(TgManageMsgBot bot) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException
    {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // прокси объект над сессией, чтобы не получать CurrentSession() через SessionFactory в методах Repository,
        // а получать возвращаемое CurrentSession() значение при обычном обращении к зависимости EntityManager внутри Repository.
        var proxySession = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));

        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var languageReadMapper = new LanguageReadMapper();
        var filmReadMapper = new FilmReadMapper(languageReadMapper);
        var languageRepository = new LanguageRepository(proxySession);
        var filmCreateMapper = new FilmCreateMapper(languageRepository);
        var filmRepository = new FilmRepository(proxySession);

        // Прокси объект над FilmService, чтобы динамически открывать / закрывать транзакции в его методах, помеченных @Transactional.
        // А не прописывать эту логику в каждом методе вручную.
        var transactionInterceptor = new TransactionInterceptor(sessionFactory);
        FilmService proxyFilmService = new ByteBuddy()
                .subclass(FilmService.class)
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(transactionInterceptor))
                .make()
                .load(FilmService.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor(Validator.class, FilmReadMapper.class, FilmCreateMapper.class, FilmRepository.class)
                .newInstance(validator, filmReadMapper, filmCreateMapper, filmRepository);


        MsgReceiver msgReceiver = new MsgReceiver(bot);
        msgReceiver.addMsgListener(new CmdMsgListener());
        msgReceiver.addMsgListener(new HomeBtnMsgListener(proxyFilmService));
        msgReceiver.addMsgListener(new TittleMsgListener(proxyFilmService));
        msgReceiver.addCallbackListener(new CategoriesCallbackListener(proxyFilmService));
        msgReceiver.addCallbackListener(new TittleCallbackListener(proxyFilmService));

        var receiverThread = new Thread(msgReceiver);
        receiverThread.setName("MsgReceiver");
        receiverThread.start();

        MsgSender msgSender = new MsgSender(bot);
        var senderThread = new Thread(msgSender);
        senderThread.setName("MsgSender");
        senderThread.start();

        return new ServiceResources(receiverThread, senderThread);
    }


    public static void terminateServiceExecution(ServiceResources serviceResources)
    {
        serviceResources.getMsgReceiver().interrupt();
        serviceResources.getMsgSender().interrupt();
        HibernateUtil.closeSessionFactory();
    }


    public static TgManageMsgBot createBot() throws TelegramApiException
    {
        TgManageMsgBot bot = new TgMovieBot();

        SetMyCommands commandsSet = SetMyCommands
                .builder()
                .commands(Arrays.stream(Command.values())
                        .map(cmd -> new BotCommand(cmd.getCmdSyntax(), cmd.getDescription()))
                        .toList())
                .build();
        bot.executeAsync(commandsSet);

        return bot;
    }
}
