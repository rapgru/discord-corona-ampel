package com.rapgru.ampel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DistrictDTO {

    private String type;
    private String gkz;
    private String name;

    @JsonProperty("Region")
    public String getType() {
        return type;
    }

    @JsonProperty("Region")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("GKZ")
    public String getGkz() {
        return gkz;
    }

    @JsonProperty("GKZ")
    public void setGkz(String gkz) {
        this.gkz = gkz;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }
}
