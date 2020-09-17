package com.rapgru.ampel.dao;

import com.j256.ormlite.dao.Dao;
import com.rapgru.ampel.object.DataFetchDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

public class DataFetchDaoImpl implements DataFetchDao {
    private final Dao<DataFetchDo, Long> dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFetchDaoImpl.class);

    public DataFetchDaoImpl(Dao<DataFetchDo, Long> dao) {
        this.dao = dao;
    }

    @Override
    public void storeDataFetch(DataFetchDo dataFetch) throws SQLException {
        LOGGER.debug("Storing DataFetch {}", dataFetch.getDate());
        dao.create(dataFetch);
    }

    @Override
    public Optional<DataFetchDo> getLastDataFetch() throws SQLException {
        return dao.queryForAll()
                .stream()
                .max(Comparator.comparing(DataFetchDo::getDate));
    }

    @Override
    public void init(DataFetchDo dataFetchDO) throws SQLException {
        dao.assignEmptyForeignCollection(dataFetchDO, "districtDataDOS");
    }
}
