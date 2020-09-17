package com.rapgru.ampel.object;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "dataFetches")
public class DataFetchDo {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String date;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<DistrictDataDo> districtDataDos;

    public DataFetchDo() {
    }

    public DataFetchDo(String date) {
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

    public List<DistrictDataDo> getDistrictDataDos() {
        return new ArrayList<>(districtDataDos);
    }

    public void setDistrictDataDos(List<DistrictDataDo> districtDataDos) {
        this.districtDataDos.clear();
        this.districtDataDos.addAll(districtDataDos);
    }
}
