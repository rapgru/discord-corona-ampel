package com.rapgru.ampel.service.data;

import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.District;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CoronaDataService {

    CompletableFuture<DataFetch> getCurrentCoronaData();

    List<District> getAllAustrianDistricts();
}
