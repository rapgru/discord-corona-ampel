package com.rapgru.ampel.object;

import javax.persistence.*;

@Table(name="t_district")
public class DistrictDataDo {


    private long id;

    private long datafetchid;

    private int gkz;

    private String warningcolor;

    private String name;

    private String reason;

    public int getGkz() {
        return gkz;
    }

    public void setGkz(int gkz) {
        this.gkz = gkz;
    }

    public String getWarningcolor() {
        return warningcolor;
    }

    public void setWarningcolor(String warningcolor) {
        this.warningcolor = warningcolor;
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

    public DistrictDataDo(long datafetchid, int gkz, String warningcolor, String name, String reason) {
        this.datafetchid = datafetchid;
        this.gkz = gkz;
        this.warningcolor = warningcolor;
        this.name = name;
        this.reason = reason;
    }

    public DistrictDataDo() {
    }

    public long getDatafetchid() {
        return datafetchid;
    }

    public void setDatafetchid(long datafetchid) {
        this.datafetchid = datafetchid;
    }
}
