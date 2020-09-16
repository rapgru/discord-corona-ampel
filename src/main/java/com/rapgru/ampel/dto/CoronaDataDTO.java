package com.rapgru.ampel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CoronaDataDTO {

    private String version;
    private String versionDate;
    private List<DistrictDTO> districts;
    private List<WeekDTO> weeks;

    @JsonProperty("VersionsNr")
    public String getVersion() {
        return version;
    }

    @JsonProperty("VersionsNr")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("VersionDate")
    public String getVersionDate() {
        return versionDate;
    }

    @JsonProperty("VersionDate")
    public void setVersionDate(String versionDate) {
        this.versionDate = versionDate;
    }

    @JsonProperty("Regionen")
    public List<DistrictDTO> getDistricts() {
        return districts;
    }

    @JsonProperty("Regionen")
    public void setDistricts(List<DistrictDTO> districts) {
        this.districts = districts;
    }

    @JsonProperty("Kalenderwochen")
    public List<WeekDTO> getWeeks() {
        return weeks;
    }

    @JsonProperty("Kalenderwochen")
    public void setWeeks(List<WeekDTO> weeks) {
        this.weeks = weeks;
    }
}
