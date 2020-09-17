package com.rapgru.ampel.dao;

import com.rapgru.ampel.object.DataFetchDo;

import java.sql.SQLException;
import java.util.Optional;

public interface DataFetchDao {

    void storeDataFetch(DataFetchDo dataFetch) throws SQLException;

    Optional<DataFetchDo> getLastDataFetch() throws SQLException;

    void init(DataFetchDo dataFetchDO) throws SQLException;
}
