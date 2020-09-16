package com.rapgru.ampel.object;

import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.WarningColor;

import javax.persistence.*;

@Table(name = "district")
public class DistrictDataDO {

    @Id
    @GeneratedValue
    private long id;

    private int gkz;

    @Enumerated(EnumType.ORDINAL)
    private WarningColor warningColor;

    private String name;
    private String reason;

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

    public DistrictDataDO(int gkz, WarningColor warningColor, String name, String reason) {
        this.gkz = gkz;
        this.warningColor = warningColor;
        this.name = name;
        this.reason = reason;
    }

    public DistrictDataDO() {
    }
}
