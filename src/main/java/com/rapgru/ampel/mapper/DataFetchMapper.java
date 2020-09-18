package com.rapgru.ampel.mapper;

import com.rapgru.ampel.dto.CoronaDataDTO;
import com.rapgru.ampel.dto.DistrictDTO;
import com.rapgru.ampel.dto.WeekDTO;
import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.DistrictData;
import com.rapgru.ampel.model.WarningColor;
import com.rapgru.ampel.object.DataFetchDo;
import com.rapgru.ampel.object.DistrictDataDo;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataFetchMapper {

    public Optional<DataFetch> mapToCoronaFetch(CoronaDataDTO coronaDataDTO) {
        if(!coronaDataDTO.getVersion().equals("2.0.0"))
            return Optional.empty();

        Optional<WeekDTO> latestWeekDTO = coronaDataDTO.getWeeks().stream()
                .max(Comparator.comparingInt(WeekDTO::getWeek));

        if(latestWeekDTO.isEmpty())
            return Optional.empty();

        return Optional.of(
                new DataFetch(
                    Instant.now(),
                    mapToDistrictDataList(
                            latestWeekDTO.get(),
                            coronaDataDTO.getDistricts()
                    )
                )
        );
    }

    private List<DistrictData> mapToDistrictDataList(WeekDTO currentWeekDTO, List<DistrictDTO> districtDTOS){
        return currentWeekDTO.getDistrictDataDTOList().stream()
                .map(districtDataDTO -> Tuple.tuple(districtDataDTO,
                            districtDTOS.stream().filter(d -> d.getGkz().equals(districtDataDTO.getGkz())).findAny()
                        ))
                .filter(t -> t.v2.isPresent())
                .map(t -> t.map2(Optional::get))
                .filter(t -> t.v2.getType().equals("Gemeinde"))
                .map(d -> new DistrictData(
                        toWarningColor(d.v1.getWarningColor()).orElse(WarningColor.GREEN),
                        mapToDistrict(d.v2),
                        d.v1.getReason()
                ))
                .collect(Collectors.toList());
    }

    private District mapToDistrict(DistrictDTO districtDTO) {
        return new District(
                Integer.parseInt(districtDTO.getGkz()),
                districtDTO.getName()
        );
    }

    private Optional<WarningColor> toWarningColor(String warningNumber) {
        switch (warningNumber) {
            case "1": return Optional.of(WarningColor.GREEN);
            case "2": return Optional.of(WarningColor.YELLOW);
            case "3": return Optional.of(WarningColor.ORANGE);
            case "4": return Optional.of(WarningColor.RED);
            default: return Optional.empty();
        }
    }

    public DataFetchDo toDataFetchDO(DataFetch dataFetch) {
        return new DataFetchDo(
                dataFetch.getDate().toString()
        );
    }

    public List<DistrictDataDo> toDistrictDataDoList(DataFetch dataFetch, DataFetchDo dataFetchDo) {
        return dataFetch.getDistrictDataList()
                .stream()
                .map(t -> toDistrictDataDO(t, dataFetchDo.getId()))
                .collect(Collectors.toList());
    }

    private DistrictDataDo toDistrictDataDO(DistrictData districtData, long id) {
        return new DistrictDataDo(
                id,
                districtData.getDistrict().getGkz(),
                districtData.getWarningColor().name(),
                districtData.getDistrict().getName(),
                districtData.getReason()
        );
    }

    public DataFetch toDataFetch(Tuple2<DataFetchDo, List<DistrictDataDo>> dataFetchDO) {
        return new DataFetch(
                Instant.parse(dataFetchDO.v1.getDate()),
                dataFetchDO.v2
                        .stream()
                        .map(this::toDistrictData)
                        .collect(Collectors.toList())
        );
    }

    private DistrictData toDistrictData(DistrictDataDo districtDataDO) {
        return new DistrictData(
                WarningColor.valueOf(districtDataDO.getWarningcolor()),
                new District(
                        districtDataDO.getGkz(),
                        districtDataDO.getName()
                ),
                districtDataDO.getReason()
        );
    }

}
