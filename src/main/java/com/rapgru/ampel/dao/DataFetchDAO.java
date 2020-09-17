package com.rapgru.ampel.dao;

import com.rapgru.ampel.object.DataFetchDO;

import java.sql.SQLException;
import java.util.Optional;

public interface DataFetchDAO {

    void storeDataFetch(DataFetchDO dataFetch) throws SQLException;

    Optional<DataFetchDO> getLastDataFetch() throws SQLException;

    void init(DataFetchDO dataFetchDO) throws SQLException;
}
