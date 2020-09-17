package com.rapgru.ampel.object;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.rapgru.ampel.model.WarningColor;

@DatabaseTable(tableName="districts")
public class DistrictDataDo {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private int gkz;

    @DatabaseField
    private WarningColor warningColor;

    @DatabaseField
    private String name;

    @DatabaseField
    private String reason;

    @DatabaseField(canBeNull = false, foreign = true)
    private DataFetchDo dataFetch;

    public int getGkz() {
        return gkz;
    }

    public void setGkz(int gkz) {
        this.gkz = gkz;
    }

    public WarningColor getWarningColor() {
        return warningColor;
    }

    public void setWarningColor(WarningColor warningColor) {
        this.warningColor = warningColor;
    }

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

    public DistrictDataDo(int gkz, WarningColor warningColor, String name, String reason) {
        this.gkz = gkz;
        this.warningColor = warningColor;
        this.name = name;
        this.reason = reason;
    }

    public DistrictDataDo() {
    }
}
