package com.rapgru.ampel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.rapgru.ampel.discord.DiscordBot;
import com.rapgru.ampel.discord.events.MessageReceivedListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import com.rapgru.ampel.dao.ConnectionManager;
import com.rapgru.ampel.dao.DataFetchDao;
import com.rapgru.ampel.dao.DataFetchDaoImpl;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.object.DataFetchDo;
import com.rapgru.ampel.service.data.*;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.difference.DistrictDifferenceServiceImpl;
import com.rapgru.ampel.service.discord.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, InterruptedException{
        validateProgramArguments(args); // check if args equals 0
        try {
            //ConnectionManager.createTables();

            Dao<DataFetchDo, Long> dataFetchDBDAO = DaoManager.createDao(ConnectionManager.getDatabase(), DataFetchDo.class);
            DataFetchDao dataFetchDAO = new DataFetchDaoImpl(dataFetchDBDAO);
            DataFetchMapper dataFetchMapper = new DataFetchMapper(dataFetchDAO);
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

            DiscordBot discordBot = setupDiscordBotWithListeners(
                    new MessageReceivedListener()
            );
            discordBot.connectBlocking();

            // graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown(discordBot)));
        } catch (Exception e) {
            LOGGER.error("Error in main", e);
        }
    }

    private static void validateProgramArguments(String[] arguments) {
        if (arguments.length != 0) {
            throw new IllegalArgumentException("no arguments allowed for this application");
        }
    }

    private static DiscordBot setupDiscordBotWithListeners(Object... listeners) throws LoginException {
        String token = System.getenv("DISCORD_KEY");
        DiscordBot discordBot = new DiscordBot(token);

        discordBot.registerEventListeners(listeners);

        return discordBot;
    }

    private static void shutdown(DiscordBot discordBot) {
        System.out.println("shutting down"); //TODO: logging
        discordBot.disconnect();
    }


}
