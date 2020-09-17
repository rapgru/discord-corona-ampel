package com.rapgru.ampel;

import com.rapgru.ampel.dao.ConnectionManager;
import com.rapgru.ampel.dao.DataFetchDao;
import com.rapgru.ampel.dao.DataFetchDaoImpl;
import com.rapgru.ampel.discord.DiscordBot;
import com.rapgru.ampel.discord.commands.DirectMessageCommand;
import com.rapgru.ampel.discord.commands.PingCommand;
import com.rapgru.ampel.discord.commands.StopCommand;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.service.data.*;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.difference.DistrictDifferenceServiceImpl;
import com.rapgru.ampel.service.discord.NotificationService;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, InterruptedException{
        validateProgramArguments(args); // check if args equals 0

        //ConnectionManager.createTables();

        DataFetchMapper dataFetchMapper = new DataFetchMapper();
        DataFetchDao dataFetchDAO = new DataFetchDaoImpl(ConnectionManager.getDatabase(), dataFetchMapper);
        CoronaDataService coronaDataService = new CoronaDataServiceImpl(dataFetchMapper);
        DistrictDifferenceService districtDifferenceService = new DistrictDifferenceServiceImpl();
        NotificationService notificationService = new NotificationService() {
            @Override
            public void pushChanges(List<DistrictChange> changes) {
                LOGGER.info("pushing changes {}", changes);
            }
        };
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

        LOGGER.info("start discord bot and block");
        DiscordBot discordBot = setupDiscordBot();
        discordBot.connectBlocking();


        // graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("shutting down discord bot");
            discordBot.shutdown();

            LOGGER.info("stopping coronaDataFetchScheduler");
            coronaDataFetchScheduler.stop();
        }));
    }

    private static void validateProgramArguments(String[] arguments) {
        if (arguments.length != 0) {
            throw new IllegalArgumentException("no arguments allowed for this application");
        }
    }

    private static DiscordBot setupDiscordBot() throws LoginException {
        String token = System.getenv("DISCORD_KEY");
        DiscordBot discordBot = new DiscordBot(token);

        discordBot.registerCommands(
                new PingCommand(),
                new StopCommand(),
                new DirectMessageCommand()
        );

        return discordBot;
    }
}
