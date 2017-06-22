package me.borawski.hcf.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Ethan on 3/16/2017.
 */
public class MySQL {

    private Connection connection;
    private String host;
    private String database;
    private String username;
    private String password;

    /*
     * Methods
     */

    public MySQL(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;

        openConnection();
    }

    private void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Getters
     */

    public Connection getConnection() {
        if (connection == null) {
            openConnection();
            return connection;
        }

        try {
            if (connection.isClosed()) {
                openConnection();
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet queryResult(String table, String key, String searchFor) {
        ResultSet set = null;
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE key=" + searchFor);
            while (statement.executeQuery().next()) {
                set = statement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static MySQL newInstance(String host, String database, String username, String password) {
        return new MySQL(host, database, username, password);
    }

}
