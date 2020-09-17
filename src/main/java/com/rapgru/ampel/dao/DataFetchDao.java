package com.rapgru.ampel.dao;

import com.rapgru.ampel.model.DataFetch;
import com.rapgru.ampel.object.DataFetchDo;

import java.sql.SQLException;
import java.util.Optional;

public interface DataFetchDao {

    void storeDataFetch(DataFetch dataFetch);

    Optional<DataFetch> getLastDataFetch();

}
