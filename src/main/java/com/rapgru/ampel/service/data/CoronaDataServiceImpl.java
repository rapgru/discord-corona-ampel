package com.rapgru.ampel.service.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapgru.ampel.dto.CoronaDataDTO;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.DistrictData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CoronaDataServiceImpl implements CoronaDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoronaDataServiceImpl.class);
    private static final String CORONA_DATA_URL = "https://corona-ampel.gv.at/sites/corona-ampel.gv.at/files/coronadata/CoronaKommissionV2.json";

    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private List<District> allDistricts = null;
    private final DataFetchMapper dataFetchMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoronaDataServiceImpl(DataFetchMapper dataFetchMapper) {
        this.dataFetchMapper = dataFetchMapper;
    }

    @Override
    public CompletableFuture<DataFetch> getCurrentCoronaData() {
        HttpRequest req = getCoronaDataRequest();
        return performCoronaDataRequest(req);
    }

    private CompletableFuture<DataFetch> performCoronaDataRequest(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(o -> {
                    LOGGER.info("Request made: {} {} - Result {}", request.method(), request.uri(), o);
                    return o;
                })
                .thenApply(this::mapToDTO)
                .thenApply(dataFetchMapper::mapToCoronaFetch);
    }
    private HttpRequest getCoronaDataRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(CORONA_DATA_URL))
                .GET()
                .build();
    }

    @Override
    public List<District> getAllAustrianDistricts() {
        if(allDistricts == null) {
            allDistricts = fetchAllDistricts();
        }

        return allDistricts;
    }

    public List<District> fetchAllDistricts() {
        return getCurrentCoronaData()
                .thenApply(dataFetch ->
                    dataFetch.getDistrictDataList().stream()
                            .map(DistrictData::getDistrict)
                            .collect(Collectors.toList())
                )
                .join();
    }

    private CoronaDataDTO mapToDTO(HttpResponse<String> response) {
        if (response.statusCode() != 200)
            throw new RequestException("Status code is not 200 but " + response.statusCode());

        try {
            return objectMapper.readValue(response.body(), CoronaDataDTO.class);
        } catch (IOException e) {
            throw new RequestException("JSON Parsing error", e);
        }
    }
}
