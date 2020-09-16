package com.rapgru.ampel.model;

public class DistrictData {

    private final WarningColor warningColor;
    private final District district;
    private final String reason;

    public DistrictData(WarningColor warningColor, District district, String reason) {
        this.warningColor = warningColor;
        this.district = district;
        this.reason = reason;
    }

    public WarningColor getWarningColor() {
        return warningColor;
    }

    public District getDistrict() {
        return district;
    }

    public String getReason() {
        return reason;
    }
}
