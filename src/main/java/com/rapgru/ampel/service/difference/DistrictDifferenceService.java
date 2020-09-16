package com.rapgru.ampel.service.difference;

import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.model.DistrictData;

import java.util.List;

public interface DistrictDifferenceService {

    List<DistrictChange> changes(List<DistrictData> prev, List<DistrictData> after);

}
