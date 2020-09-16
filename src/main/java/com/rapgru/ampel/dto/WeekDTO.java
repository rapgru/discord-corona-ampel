package com.rapgru.ampel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WeekDTO {

    private int week;
    private List<DistrictDataDTO> districtDataDTOList;

    @JsonProperty("KW")
    public int getWeek() {
        return week;
    }

    @JsonProperty("KW")
    public void setWeek(int week) {
        this.week = week;
    }

    @JsonProperty("Warnstufen")
    public List<DistrictDataDTO> getDistrictDataDTOList() {
        return districtDataDTOList;
    }

    @JsonProperty("Warnstufen")
    public void setDistrictDataDTOList(List<DistrictDataDTO> districtDataDTOList) {
        this.districtDataDTOList = districtDataDTOList;
    }
}
