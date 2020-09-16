package com.rapgru.ampel.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapgru.ampel.dao.DataFetchDAO;
import com.rapgru.ampel.dto.CoronaDataDTO;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.service.difference.DistrictDifferenceService;
import com.rapgru.ampel.service.discord.NotificationService;
import org.jooq.lambda.tuple.Tuple;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RefreshDataTask implements Runnable {

    private final CoronaDataService coronaDataService;

    private final DataFetchDAO dataFetchDAO;
    private final DataFetchMapper dataFetchMapper;
    private final DistrictDifferenceService districtDifferenceService;
    private final NotificationService notificationService;

    public RefreshDataTask(CoronaDataService coronaDataService, DataFetchDAO dataFetchDAO, DataFetchMapper dataFetchMapper, DistrictDifferenceService districtDifferenceService, NotificationService notificationService) {
        this.coronaDataService = coronaDataService;
        this.dataFetchDAO = dataFetchDAO;
        this.dataFetchMapper = dataFetchMapper;
        this.districtDifferenceService = districtDifferenceService;
        this.notificationService = notificationService;
    }


    @Override
    public void run() {
        fetchData();
    }

    private void fetchData() {
        coronaDataService.getCurrentCoronaData()
                .thenApply(dataFetch -> Tuple.tuple(dataFetchDAO.getLastDataFetch(), dataFetch))
                .thenApply(dataFetches -> dataFetches.map1(f -> f.map(dataFetchMapper::toDataFetch)))
                .thenAccept(dataFetches -> {
                    dataFetchDAO.storeDataFetch(dataFetchMapper.toDO(dataFetches.v2));

                    DataFetch prevDataFetch = dataFetches.v1
                            .orElseThrow(() -> new RequestException("Unpopulated database - will be successful on next execution"));

                    List<DistrictChange> changes =
                        districtDifferenceService.changes(prevDataFetch.getDistrictDataList(), dataFetches.v2.getDistrictDataList());

                    notificationService.pushChanges(changes);
                });
    }


}
