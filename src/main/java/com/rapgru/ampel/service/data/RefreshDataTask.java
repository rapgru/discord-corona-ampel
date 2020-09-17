package com.rapgru.ampel.service.data;

import com.rapgru.ampel.dao.DataFetchDao;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.discord.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RefreshDataTask implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(RefreshDataTask.class);
    private final CoronaDataService coronaDataService;

    private final DataFetchDao dataFetchDAO;
    private final DataFetchMapper dataFetchMapper;
    private final DistrictDifferenceService districtDifferenceService;
    private final NotificationService notificationService;

    public RefreshDataTask(CoronaDataService coronaDataService, DataFetchDao dataFetchDAO, DataFetchMapper dataFetchMapper, DistrictDifferenceService districtDifferenceService, NotificationService notificationService) {
        this.coronaDataService = coronaDataService;
        this.dataFetchDAO = dataFetchDAO;
        this.dataFetchMapper = dataFetchMapper;
        this.districtDifferenceService = districtDifferenceService;
        this.notificationService = notificationService;
    }


    @Override
    public void run() {
        try {
            LOGGER.info("starting scheduled fetch data task");
            fetchData();
        } catch (Exception e) {
            LOGGER.error("General error", e);
        }
    }

    private void fetchData() {
        Optional<DataFetch> optionalDataFetch = coronaDataService.getCurrentCoronaData();
        if(optionalDataFetch.isEmpty()) {
            LOGGER.warn("CoronaDataService did not supply data");
        } else {
            DataFetch dataFetch = optionalDataFetch.get();

            Optional<DataFetch> optionalPrevDataFetch = dataFetchDAO.getLastDataFetch();
            if(optionalPrevDataFetch.isEmpty()) {
                LOGGER.warn("Unpopulated database - will be successful on next execution");
                dataFetchDAO.storeDataFetch(dataFetch);
            } else {
                DataFetch prevDataFetch = optionalPrevDataFetch.get();

                List<DistrictChange> changes =
                        districtDifferenceService.changes(prevDataFetch.getDistrictDataList(), dataFetch.getDistrictDataList());

                LOGGER.info("calculated differences: {}", changes);

                // Only store datafetch when something changed
                if(!changes.isEmpty()) {
                    dataFetchDAO.storeDataFetch(dataFetch);
                    notificationService.pushChanges(changes);
                }
            }
        }
    }


}
