package com.rapgru.ampel.mapper;

import com.rapgru.ampel.dao.DataFetchDao;
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

import java.sql.SQLException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataFetchMapper {

    private final DataFetchDao dataFetchDAO;

    public DataFetchMapper(DataFetchDao dataFetchDAO) {
        this.dataFetchDAO = dataFetchDAO;
    }

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
        return districtDTOS.stream()
                .filter(districtDTO -> districtDTO.getType().equals("Gemeinde"))
                .map(districtDTO -> mapToDistrictData(currentWeekDTO, districtDTO))
                .collect(Collectors.toList());
    }

    private DistrictData mapToDistrictData(WeekDTO currentWeekDTO, DistrictDTO districtDTO){
        return currentWeekDTO.getDistrictDataDTOList()
                .stream()
                .filter(d -> d.getGkz().equals(districtDTO.getGkz()))
                .findAny()
                .map(d -> Tuple.tuple(d, toWarningColor(d.getWarningColor())))
                .filter(d -> d.v2.isPresent())
                .map(d -> new DistrictData(
                        d.v2.get(),
                        mapToDistrict(districtDTO),
                        d.v1.getReason()
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

    private Optional<WarningColor> toWarningColor(String warningNumber) {
        switch (warningNumber) {
            case "1": return Optional.of(WarningColor.GREEN);
            case "2": return Optional.of(WarningColor.YELLOW);
            case "3": return Optional.of(WarningColor.ORANGE);
            case "4": return Optional.of(WarningColor.RED);
            default: return Optional.empty();
        }
    }

    public DataFetchDo toDO(DataFetch dataFetch) throws SQLException {
        DataFetchDo dataFetchDO = new DataFetchDo(
                dataFetch.getDate().toString()
        );
        dataFetchDAO.init(dataFetchDO);
        dataFetchDO.setDistrictDataDos(
                dataFetch.getDistrictDataList()
                        .stream()
                        .map(this::toDistrictDataDO)
                        .collect(Collectors.toList())
        );
        return dataFetchDO;
    }

    private DistrictDataDo toDistrictDataDO(DistrictData districtData) {
        return new DistrictDataDo(
                districtData.getDistrict().getGkz(),
                districtData.getWarningColor(),
                districtData.getDistrict().getName(),
                districtData.getReason()
        );
    }

    public DataFetch toDataFetch(DataFetchDo dataFetchDO) {
        return new DataFetch(
                Instant.parse(dataFetchDO.getDate()),
                dataFetchDO.getDistrictDataDos()
                        .stream()
                        .map(this::toDistrictData)
                        .collect(Collectors.toList())
        );
    }

    private DistrictData toDistrictData(DistrictDataDo districtDataDO) {
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
