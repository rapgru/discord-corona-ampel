package com.rapgru.ampel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DistrictDataDTO {

    private String gkz;
    private String warningColor;
    private String reason;

    @JsonProperty("GKZ")
    public String getGkz() {
        return gkz;
    }

    @JsonProperty("GKZ")
    public void setGkz(String gkz) {
        this.gkz = gkz;
    }

    @JsonProperty("Warnstufe")
    public String getWarningColor() {
        return warningColor;
    }

    @JsonProperty("Warnstufe")
    public void setWarningColor(String warningColor) {
        this.warningColor = warningColor;
    }

    @JsonProperty("Begruendung")
    public String getReason() {
        return reason;
    }

    @JsonProperty("Begruendung")
    public void setReason(String reason) {
        this.reason = reason;
    }
}
