package com.rapgru.ampel.object;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name="dataFetch")
public class DataFetchDO {

    private long id;

    private String date;

    private List<DistrictDataDO> districtDataDOS;

    public DataFetchDO() {
    }

    public DataFetchDO(String date, List<DistrictDataDO> districtDataDOS) {
        this.date = date;
        this.districtDataDOS = districtDataDOS;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DistrictDataDO> getDistrictDataDOS() {
        return districtDataDOS;
    }

    public void setDistrictDataDOS(List<DistrictDataDO> districtDataDOS) {
        this.districtDataDOS = districtDataDOS;
    }
}
