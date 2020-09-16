package com.rapgru.ampel.model;

import javax.persistence.Table;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class DataFetch {

    private final Instant date;
    private final List<DistrictData> districtDataList;

    public DataFetch(Instant date, List<DistrictData> districtDataList) {
        this.date = date;
        this.districtDataList = districtDataList;
    }

    public Instant getDate() {
        return date;
    }

    public List<DistrictData> getDistrictDataList() {
        return districtDataList;
    }
}
