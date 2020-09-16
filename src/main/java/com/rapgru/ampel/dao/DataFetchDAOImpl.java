package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.rapgru.ampel.object.DataFetchDO;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DataFetchDAOImpl implements DataFetchDAO {
    private final Database database;

    public DataFetchDAOImpl(Database database) {
        this.database = database;
    }

    @Override
    public void storeDataFetch(DataFetchDO dataFetch) {
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
