package com.rapgru.ampel.dao;

import com.dieselpoint.norm.Database;
import com.dieselpoint.norm.sqlmakers.PostgresMaker;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.rapgru.ampel.object.DataFetchDo;
import com.rapgru.ampel.object.DistrictDataDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    private static Database database = null;

    public static Database getDatabase() {
        if(database == null) {
            Database db = new Database();

            URI dbUri = URI.create(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

            db.setJdbcUrl(dbUrl);
            db.setPassword(password);
            db.setUser(username);
            db.setSqlMaker(new PostgresMaker());

            database = db;
        }

        return database;
    }

    public static void createTables() {
        getDatabase().createTable(DataFetchDo.class);
        getDatabase().createTable(DistrictDataDo.class);
    }

}
