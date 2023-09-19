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
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Scanner;


public class BotStarter
{

    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;


    public static void startBot(TgManageMsgBot bot)
    {
        try
        {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public static void startServices(TgManageMsgBot bot) throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException
    {
        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory())
        {
            // прокси объект над сессией, чтобы не получать CurrentSession() через SessionFactory в методах Repository,
            // а просто при обращении к зависимости EntityManager внутри Repository.
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

            // Daemon потоки можно будет заменить обычными с interrupt(),
            // но для этого нужно будет сделать консольное приложение из проекта. Чтобы где-то вводить stop,
            // оповещать потоки о прекращении работы и они завершали своё выполнение
            Thread receiver = new Thread(msgReceiver);
            receiver.setDaemon(true);
            receiver.setName("MsgReceiver");
            receiver.setPriority(PRIORITY_FOR_RECEIVER);
            receiver.start();

            MsgSender msgSender = new MsgSender(bot);
            Thread sender = new Thread(msgSender);
            sender.setDaemon(true);
            sender.setName("MsgSender");
            sender.setPriority(PRIORITY_FOR_SENDER);
            sender.start();


            // Просто чтобы не закрывать sessionFactory.
            // Потом переделать это и потоки-демоны в обычные потоки и нормальное приложение
            Scanner scanner = new Scanner(System.in);
            scanner.nextInt();
        }
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
