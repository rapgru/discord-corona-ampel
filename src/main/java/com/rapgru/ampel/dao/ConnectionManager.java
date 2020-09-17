package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rapgru.ampel.object.DataFetchDO;
import com.rapgru.ampel.object.DistrictDataDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    private static ConnectionSource database = null;

    public static ConnectionSource getDatabase() throws SQLException {
        if(database == null) {

            URI dbUri = URI.create(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

            database = new JdbcConnectionSource(dbUrl, username, password);
        }

        return database;
    }

    public static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(getDatabase(), DataFetchDO.class);
        TableUtils.createTableIfNotExists(getDatabase(), DistrictDataDO.class);
    }

}
