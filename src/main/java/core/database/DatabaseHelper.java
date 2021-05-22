package core.database;

import core.Log;

import java.sql.*;

public class DatabaseHelper {

    public Connection connectData(String dbUrl, String dbUser, String dbPass) {
        Connection connection;

        Log.info("Connect MySQL JDBC");
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            Log.highlight("Connect MySQL JDBC success");
        } catch (SQLException ex) {
            Log.errorAndStop("Have error when connecting database: " + ex);
            return null;
        }
        return connection;
    }

    public void selectData(String sqlStatement, Connection conn) {
        ResultSet rs;
        PreparedStatement prepareStat;
        try {
            Log.debug("Execute query: " + sqlStatement);
            prepareStat = conn.prepareStatement(sqlStatement);
            rs = prepareStat.executeQuery();

            while (rs.next()) {
                Log.debug(rs.getString(1));
            }
        } catch (SQLException ex) {
            Log.errorAndStop("Have error when query statement: " + ex);
        }
    }

    public ResultSet getDataResultSet(String sqlStatement, Connection conn) {
        ResultSet rs = null;
        PreparedStatement prepareStat;
        try {
            Log.debug("Execute query : " + sqlStatement);
            prepareStat = conn.prepareStatement(sqlStatement);
            rs = prepareStat.executeQuery();

        } catch (SQLException ex) {
            Log.errorAndStop("Have error when query statement : " + ex);
        }

        return rs;
    }

    public void updateData(String sqlStatement, Connection conn) {
        PreparedStatement prepareStat = null;
        try {
            Log.debug("Execute update statement: " + sqlStatement);
            prepareStat = conn.prepareStatement(sqlStatement);
            int updatedRow = prepareStat.executeUpdate();
            Log.debug("Execute update statement success! Updated row: " + updatedRow);
        } catch (SQLException ex) {
            Log.errorAndStop("Have error when query statement: " + ex);
        } finally {
            try {
                if (prepareStat != null)
                    prepareStat.close();
            } catch (SQLException ex) {
                Log.errorAndStop("Have error when query statement: " + ex);
            }
        }
    }

    public void closeConnection(Connection conn) {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException ex) {
                Log.errorAndStop("Have error when closing connection: " + ex);
            }
    }
}
