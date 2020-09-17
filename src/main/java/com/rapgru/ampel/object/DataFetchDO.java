package com.rapgru.ampel.object;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "dataFetches")
public class DataFetchDO {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String date;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<DistrictDataDO> districtDataDOS;

    public DataFetchDO() {
    }

    public DataFetchDO(String date) {
        this.date = date;
    }

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
        return new ArrayList<>(districtDataDOS);
    }

    public void setDistrictDataDOS(List<DistrictDataDO> districtDataDOS) {
        this.districtDataDOS.clear();
        this.districtDataDOS.addAll(districtDataDOS);
    }
}
