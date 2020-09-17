package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.j256.ormlite.dao.Dao;
import com.rapgru.ampel.object.DataFetchDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DataFetchDAOImpl implements DataFetchDAO {
    private final Dao<DataFetchDO, Long> dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFetchDAOImpl.class);

    public DataFetchDAOImpl(Dao<DataFetchDO, Long> dao) {
        this.dao = dao;
    }

    @Override
    public void storeDataFetch(DataFetchDO dataFetch) throws SQLException {
        LOGGER.debug("Storing DataFetch {}", dataFetch.getDate());
        dao.create(dataFetch);
    }

    @Override
    public Optional<DataFetchDO> getLastDataFetch() throws SQLException {
        return dao.queryForAll()
                .stream()
                .max(Comparator.comparing(DataFetchDO::getDate));
    }

    @Override
    public void init(DataFetchDO dataFetchDO) throws SQLException {
        dao.assignEmptyForeignCollection(dataFetchDO, "districtDataDOS");
    }
}
