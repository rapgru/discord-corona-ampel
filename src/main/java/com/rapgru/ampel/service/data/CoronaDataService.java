package com.rapgru.ampel.service.data;

import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.District;

import java.util.List;
import java.util.Optional;

public interface CoronaDataService {

    Optional<DataFetch> getCurrentCoronaData();

    List<District> getAllAustrianDistricts();

    Optional<District> getDistrictByName(String districtName, boolean ignoreCase);
}
