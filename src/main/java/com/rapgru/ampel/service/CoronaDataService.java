package com.rapgru.ampel.service;

import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.DistrictData;

import java.util.List;

public interface CoronaDataService {

    List<DistrictData> getCurrentDistrictData();

    List<District> getAustrianDistricts();

}
