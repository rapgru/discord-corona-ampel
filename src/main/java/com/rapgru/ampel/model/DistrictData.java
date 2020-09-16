package com.rapgru.ampel.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistrictData that = (DistrictData) o;
        return warningColor == that.warningColor &&
                district.equals(that.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warningColor, district);
    }
}
