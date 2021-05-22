package core.database;

import core.Config;
import core.PropertyValues;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

public class DBBaseAction {

    public DatabaseHelper databaseHelper;
    String mySQLHost, mySQLPort, mySQLDatabase, mySQLUser, mySQLPass, mySQLUrl;
    String propFilePath;
    PropertyValues propertyValues;

    public DBBaseAction() {
        databaseHelper = new DatabaseHelper();

        propFilePath = "config/database.properties";
        propertyValues = new PropertyValues(propFilePath);

        mySQLHost = propertyValues.getProperty(Config.globalEnvironment + "." + "mysql.host");
        mySQLPort = propertyValues.getProperty(Config.globalEnvironment + "." + "mysql.port");
        mySQLDatabase = propertyValues.getProperty(Config.globalEnvironment + "." + "mysql.database");
        mySQLUser = propertyValues.getProperty(Config.globalEnvironment + "." + "mysql.user");
        mySQLPass = propertyValues.getProperty(Config.globalEnvironment + "." + "mysql.pass");
        mySQLUrl = "jdbc:mysql://" + mySQLHost + ":" + mySQLPort + "/" + mySQLDatabase;
    }

    public Connection connectDatabase() {
        Connection conn;

        // .connectData("jdbc:mysql://10.2.9.111:6033/propzy_vietnam", "propzydeveloper", "DevTestPropzy2017!@#");
        conn = databaseHelper.connectData(mySQLUrl, mySQLUser, mySQLPass);

        return conn;
    }

    public void closeConnection(Connection conn) {
        databaseHelper.closeConnection(conn);
    }

    private ResultSet executeSelectStatement(Connection conn, String fromTable, String whereColumn, String whereValue) {
        String smDeleteLsoListingPosition = "SELECT * FROM " + fromTable
                + " where " + whereColumn + " = '" +  whereValue + "';";

        return databaseHelper.getDataResultSet(smDeleteLsoListingPosition, conn);
    }

    private ResultSet executeSelectDepartmentNameFromUserIDStatement(Connection conn, String userID) {
        String smDeleteLsoListingPosition = "SELECT DISTINCT hd.DepartmentName from hrm_users hu " +
                "JOIN hrm_user_department hud ON hud.UserID = hu.UserID " +
                "JOIN hrm_departments hd ON hd.ID = hud.DepartmentID " +
                "JOIN hrm_user_map_position hump ON hump.UserID = hu.UserID " +
                "JOIN hrm_user_positions hup ON hup.DepartmentID = hump.DepartmentID " +
                "where hu.UserID = " + userID + " AND hd.DepartmentType = 'Zone' AND hu.IsDeleted = 0 AND hu.statusID = 2;";

        return databaseHelper.getDataResultSet(smDeleteLsoListingPosition, conn);
    }

    private void executeUpdateStatement(Connection conn, String updateTable, Map<String, String> mapUpdateData,
                                            String whereColumn, String whereValue) {

        String setQuery = " SET ";

        int i = 0;
        for (Map.Entry<String, String> entry : mapUpdateData.entrySet()) {
            if (i == 0) {
                setQuery += entry.getKey() + " = '" + entry.getValue() + "'";
            }

            if (i > 0) {
                setQuery += " , " + entry.getKey() + " = '" + entry.getValue() + "'";
            }

            i++;
        }

        String smUpdateData = "UPDATE " + updateTable
                + setQuery
                + " where " + whereColumn + " = '" + whereValue + "';";

        databaseHelper.updateData(smUpdateData, conn);
    }
}
