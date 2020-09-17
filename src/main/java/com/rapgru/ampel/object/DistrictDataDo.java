package com.rapgru.ampel.object;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rapgru.ampel.model.WarningColor;

import javax.persistence.*;

@Table(name="t_district")
public class DistrictDataDo {


    private long id;

    private long dataFetchId;

    private int gkz;

    private WarningColor warningColor;

    private String name;

    private String reason;

    public int getGkz() {
        return gkz;
    }

    public void setGkz(int gkz) {
        this.gkz = gkz;
    }

    @Enumerated(EnumType.ORDINAL)
    public WarningColor getWarningColor() {
        return warningColor;
    }

    public void setWarningColor(WarningColor warningColor) {
        this.warningColor = warningColor;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DistrictDataDo(long dataFetchId, int gkz, WarningColor warningColor, String name, String reason) {
        this.dataFetchId = dataFetchId;
        this.gkz = gkz;
        this.warningColor = warningColor;
        this.name = name;
        this.reason = reason;
    }

    public DistrictDataDo() {
    }

    public long getDataFetchId() {
        return dataFetchId;
    }

    public void setDataFetchId(long dataFetchId) {
        this.dataFetchId = dataFetchId;
    }
}
