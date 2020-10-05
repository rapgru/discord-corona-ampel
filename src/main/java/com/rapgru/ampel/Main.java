package com.rapgru.ampel;

import com.rapgru.ampel.dao.*;
import com.rapgru.ampel.discord.DiscordBot;
import com.rapgru.ampel.discord.commands.*;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.service.data.*;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.difference.DistrictDifferenceServiceImpl;
import com.rapgru.ampel.service.discord.ChangeMessageService;
import com.rapgru.ampel.service.discord.ChangeMessageServiceImpl;
import com.rapgru.ampel.service.discord.NotificationService;
import com.rapgru.ampel.service.discord.NotificationServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, InterruptedException {
        validateProgramArguments(args); // check if args equals 0

        DataFetchMapper dataFetchMapper = new DataFetchMapper();
        DataFetchDao dataFetchDAO = new DataFetchDaoImpl(ConnectionManager.getDatabase(), dataFetchMapper);
        SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl(ConnectionManager.getDatabase());

        CoronaDataService coronaDataService = new CoronaDataServiceImpl(dataFetchMapper);
        DistrictDifferenceService districtDifferenceService = new DistrictDifferenceServiceImpl();

        DiscordBot discordBot = setupDiscordBot(subscriptionDAO, coronaDataService);

        ChangeMessageService changeMessageService = new ChangeMessageServiceImpl();
        NotificationService notificationService = new NotificationServiceListener(
                subscriptionDAO,
                discordBot,
                changeMessageService
        );

        RefreshDataTask refreshDataTask = new RefreshDataTask(
                coronaDataService,
                dataFetchDAO,
                dataFetchMapper,
                districtDifferenceService,
                notificationService
        );

        CoronaDataFetchScheduler coronaDataFetchScheduler = new CoronaDataFetchSchedulerImpl(refreshDataTask);
        coronaDataFetchScheduler.start();
        LOGGER.info("Started data fetch scheduler");

        Runnable shutdownHook = () -> {
            LOGGER.info("shutting down discord bot");
            discordBot.shutdown();

            LOGGER.info("stopping coronaDataFetchScheduler");
            coronaDataFetchScheduler.stop();
            LOGGER.info("gracefully shutdown application");
        };

        // graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
        discordBot.addShutdownHook(shutdownHook);
    }

    private static void validateProgramArguments(String[] arguments) {
        if (arguments.length != 0) {
            throw new IllegalArgumentException("no arguments allowed for this application");
        }
    }

    private static DiscordBot setupDiscordBot(SubscriptionDAO subscriptionDAO, CoronaDataService coronaDataService)
            throws LoginException, InterruptedException {
        LOGGER.info("start discord bot");
        DiscordBot discordBot = new DiscordBot(System.getenv("DISCORD_KEY"));
        discordBot.registerCommands(
                new PingCommand(),
                new StopCommand(),
                new DirectMessageCommand(),
                new NoPermissionCommand(),
                new CheckSubscriptionCommand(subscriptionDAO),
                new SubscribeCommand(subscriptionDAO, coronaDataService),
                new UnsubscribeCommand(coronaDataService, subscriptionDAO)
        );
        discordBot.awaitReady();
        LOGGER.info("discord bot started");

        return discordBot;
    }
}
