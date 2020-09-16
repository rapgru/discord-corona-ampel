package com.rapgru.ampel.mapper;

import com.rapgru.ampel.dto.CoronaDataDTO;
import com.rapgru.ampel.dto.DistrictDTO;
import com.rapgru.ampel.dto.DistrictDataDTO;
import com.rapgru.ampel.dto.WeekDTO;
import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.DistrictData;
import com.rapgru.ampel.model.WarningColor;
import com.rapgru.ampel.object.DataFetchDO;
import com.rapgru.ampel.object.DistrictDataDO;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DataFetchMapper {

    public DataFetch mapToCoronaFetch(CoronaDataDTO coronaDataDTO) {
        if(!coronaDataDTO.getVersion().equals("2.0.0"))
            throw new MappingException("unsupported schema version");

        WeekDTO latestWeekDTO = coronaDataDTO.getWeeks().stream()
                .max(Comparator.comparingInt(WeekDTO::getWeek))
                .orElseThrow(() -> new MappingException("no week data supplied"));

        return new DataFetch(
                Date.from(Instant.now()),
                mapToDistrictDataList(
                        latestWeekDTO,
                        coronaDataDTO.getDistricts()
                )
        );
    }

    private List<DistrictData> mapToDistrictDataList(WeekDTO currentWeekDTO, List<DistrictDTO> districtDTOS) {
        return districtDTOS.stream()
                .filter(districtDTO -> districtDTO.getType().equals("Gemeinde"))
                .map(districtDTO -> mapToDistrictData(currentWeekDTO, districtDTO))
                .collect(Collectors.toList());
    }

    private DistrictData mapToDistrictData(WeekDTO currentWeekDTO, DistrictDTO districtDTO) {
        return currentWeekDTO.getDistrictDataDTOList()
                .stream()
                .filter(d -> d.getGkz().equals(districtDTO.getGkz()))
                .findAny()
                .map(d -> new DistrictData(
                        toWarningColor(d.getWarningColor()),
                        mapToDistrict(districtDTO),
                        d.getReason()
                ))
                .orElse(new DistrictData(
                        WarningColor.GREEN,
                        mapToDistrict(districtDTO),
                        ""
                ));
    }

    private District mapToDistrict(DistrictDTO districtDTO) {
        return new District(
                Integer.parseInt(districtDTO.getGkz()),
                districtDTO.getName()
        );
    }

    private WarningColor toWarningColor(String warningNumber) {
        switch (warningNumber) {
            case "1": return WarningColor.GREEN;
            case "2": return WarningColor.YELLOW;
            case "3": return WarningColor.ORANGE;
            case "4": return WarningColor.RED;
            default: throw new MappingException("Unkown warning number");
        }
    }

    public DataFetchDO toDO(DataFetch dataFetch) {
        return new DataFetchDO(
                dataFetch.getDate(),
                dataFetch.getDistrictDataList()
                        .stream()
                        .map(this::toDistrictDataDO)
                        .collect(Collectors.toList())
        );
    }

    private DistrictDataDO toDistrictDataDO(DistrictData districtData) {
        return new DistrictDataDO(
                districtData.getDistrict().getGkz(),
                districtData.getWarningColor(),
                districtData.getDistrict().getName(),
                districtData.getReason()
        );
    }

    public DataFetch toDataFetch(DataFetchDO dataFetchDO) {
        return new DataFetch(
                dataFetchDO.getDate(),
                dataFetchDO.getDistrictDataDOS()
                        .stream()
                        .map(this::toDistrictData)
                        .collect(Collectors.toList())
        );
    }

    private DistrictData toDistrictData(DistrictDataDO districtDataDO) {
        return new DistrictData(
                districtDataDO.getWarningColor(),
                new District(
                        districtDataDO.getGkz(),
                        districtDataDO.getName()
                ),
                districtDataDO.getReason()
        );
    }

}
