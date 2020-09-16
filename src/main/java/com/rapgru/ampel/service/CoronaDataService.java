package com.rapgru.ampel.service;

import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.DistrictData;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CoronaDataService {

    Mono<List<DistrictData>> getCurrentDistrictData();

    Mono<List<District>> getAustrianDistricts();

}
