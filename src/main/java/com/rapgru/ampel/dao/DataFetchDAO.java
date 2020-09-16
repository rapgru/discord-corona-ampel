package com.rapgru.ampel.dao;

import com.rapgru.ampel.object.DataFetchDO;

import java.util.Optional;

public interface DataFetchDAO {
    public void storeDataFetch(DataFetchDO dataFetch);

    public Optional<DataFetchDO> getLastDataFetch();
}
