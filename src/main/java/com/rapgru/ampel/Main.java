package com.rapgru.ampel;

import com.rapgru.ampel.dao.ConnectionManager;
import com.rapgru.ampel.dao.DataFetchDAO;
import com.rapgru.ampel.dao.DataFetchDAOImpl;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.service.data.*;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.difference.DistrictDifferenceServiceImpl;
import com.rapgru.ampel.service.discord.NotificationService;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConnectionManager.createTables();

        DataFetchMapper dataFetchMapper = new DataFetchMapper();
        CoronaDataService coronaDataService = new CoronaDataServiceImpl(dataFetchMapper);
        DataFetchDAO dataFetchDAO = new DataFetchDAOImpl(ConnectionManager.getDatabase());
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

        LOGGER.info("All districts {}", coronaDataService.getAllAustrianDistricts());
    }


}
