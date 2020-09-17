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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CoronaDataServiceImpl implements CoronaDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoronaDataServiceImpl.class);
    private static final String CORONA_DATA_URL = System.getenv("DATA_URL");

    private final HttpClient httpClient = HttpClient.newBuilder().build();
    private List<District> allDistricts = null;
    private final DataFetchMapper dataFetchMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoronaDataServiceImpl(DataFetchMapper dataFetchMapper) {
        this.dataFetchMapper = dataFetchMapper;
    }

    @Override
    public Optional<DataFetch> getCurrentCoronaData() {
        HttpRequest req = getCoronaDataRequest();
        return performCoronaDataRequest(req);
    }

    private Optional<DataFetch> performCoronaDataRequest(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
             response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            LOGGER.error("IO Execption during HTTP Request for Corona Data",e);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted exception during HTTP Request for Corona Data",e);
        }

        LOGGER.info("Request made: {} {} - Result {}", request.method(), request.uri(), response);
        System.out.println(String.format("Request made: %s %s - Result %s", request.method(), request.uri(), response));

        return Optional.ofNullable(response)
                .flatMap(this::mapToDTO)
                .flatMap(dataFetchMapper::mapToCoronaFetch);
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
                .map(dataFetch ->
                    dataFetch.getDistrictDataList().stream()
                            .map(DistrictData::getDistrict)
                            .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

    private Optional<CoronaDataDTO> mapToDTO(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            LOGGER.error("Status code is not 200 but " + response.statusCode());
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(response.body(), CoronaDataDTO.class));
        } catch (JsonProcessingException e) {
            LOGGER.error("JSON Parsing error", e);
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
