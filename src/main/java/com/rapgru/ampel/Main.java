package com.rapgru.ampel;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.rapgru.ampel.dao.ConnectionManager;
import com.rapgru.ampel.dao.DataFetchDAO;
import com.rapgru.ampel.dao.DataFetchDAOImpl;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.object.DataFetchDO;
import com.rapgru.ampel.service.data.*;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.difference.DistrictDifferenceServiceImpl;
import com.rapgru.ampel.service.discord.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            //ConnectionManager.createTables();

            Dao<DataFetchDO, Long> dataFetchDBDAO = DaoManager.createDao(ConnectionManager.getDatabase(), DataFetchDO.class);
            DataFetchDAO dataFetchDAO = new DataFetchDAOImpl(dataFetchDBDAO);
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
        } catch (Exception e) {
            LOGGER.error("Error in main", e);
        }
    }


}
