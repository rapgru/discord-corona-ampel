package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.j256.ormlite.dao.Dao;
import com.rapgru.ampel.object.DataFetchDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DataFetchDAOImpl implements DataFetchDAO {
    private final Database database;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFetchDAOImpl.class);

    public DataFetchDAOImpl(Database database) {
        this.database = database;
    }

    @Override
    public void storeDataFetch(DataFetchDO dataFetch) {
        LOGGER.debug("Storing DataFetch {}", dataFetch.getDate());
        database.insert(dataFetch);
    }

    @Override
    public Optional<DataFetchDO> getLastDataFetch() {
        return database
                .results(DataFetchDO.class)
                .stream()
                .max(Comparator.comparing(DataFetchDO::getDate));
    }
}
