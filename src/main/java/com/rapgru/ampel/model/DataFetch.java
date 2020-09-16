package com.rapgru.ampel.model;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name="dataFetch")
public class DataFetch {

    private final Date date;
    private final List<DistrictData> districtDataList;

    public DataFetch(Date date, List<DistrictData> districtDataList) {
        this.date = date;
        this.districtDataList = districtDataList;
    }

    public Date getDate() {
        return date;
    }

    public List<DistrictData> getDistrictDataList() {
        return districtDataList;
    }
}
