package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.rapgru.ampel.mapper.DataFetchMapper;
import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.object.DataFetchDo;
import com.rapgru.ampel.object.DistrictDataDo;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataFetchDaoImpl implements DataFetchDao {
    private final Database database;
    private final DataFetchMapper dataFetchMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFetchDaoImpl.class);

    public DataFetchDaoImpl(Database database, DataFetchMapper dataFetchMapper) {
        this.database = database;
        this.dataFetchMapper = dataFetchMapper;
    }

    @Override
    public void storeDataFetch(DataFetch dataFetch) {
        LOGGER.debug("Storing DataFetch {}", dataFetch.getDate());
        DataFetchDo dataFetchDo = dataFetchMapper.toDataFetchDO(dataFetch);
        database.insert(dataFetchDo);

        dataFetchMapper.toDistrictDataDoList(dataFetch, dataFetchDo).forEach(database::insert);
    }

    @Override
    public Optional<DataFetch> getLastDataFetch() {
        return database
                .results(DataFetchDo.class)
                .stream()
                .max(Comparator.comparing(DataFetchDo::getDate))
                .map(dataFetchDo -> Tuple.tuple(dataFetchDo, database
                        .results(DistrictDataDo.class)
                        .stream()
                        .filter(d -> d.getDataFetchId() == dataFetchDo.getId())
                        .collect(Collectors.toList()))
                )
                .map(dataFetchMapper::toDataFetch);
    }
}
