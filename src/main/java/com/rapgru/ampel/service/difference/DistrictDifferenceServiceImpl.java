package com.rapgru.ampel.service.difference;

import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.model.DistrictChangeDirection;
import com.rapgru.ampel.model.DistrictData;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DistrictDifferenceServiceImpl implements DistrictDifferenceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistrictDifferenceServiceImpl.class);

    @Override
    public List<DistrictChange> changes(List<DistrictData> prev, List<DistrictData> after) {
        return Seq.seq(prev)
                .map(
                        districtData -> Tuple.tuple(
                                districtData,
                                Seq.seq(after)
                                        .findFirst(d -> d.getDistrict().getGkz() == districtData.getDistrict().getGkz())
                        )
                )
                .peek(t -> {
                    if (t.v2.isEmpty())
                        LOGGER.warn("GKZ {} Name {} seems to have disappeared between two weeks", t.v1.getDistrict().getGkz(), t.v1.getDistrict().getName());
                })
                .filter(t -> t.v2.isPresent())
                .map(t -> t.map2(Optional::get))
                .filter(t -> !t.v1.equals(t.v2))
                .map(t -> new DistrictChange(
                        t.v1.getWarningColor(),
                        t.v2.getWarningColor(),
                        t.v1.getWarningColor().compareTo(t.v2.getWarningColor()) > 0 ? DistrictChangeDirection.HIGHER : DistrictChangeDirection.LOWER,
                        t.v2
                ))
                .toList();
    }
}
