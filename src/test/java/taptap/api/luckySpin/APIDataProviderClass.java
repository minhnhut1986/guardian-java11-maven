package taptap.api.luckySpin;

import org.testng.annotations.DataProvider;
import core.CSVDataReader;
import core.Log;

public class APIDataProviderClass {

    @DataProvider(name = "Create new user")
    public static Object[][] initDataCreateNewUser() throws Exception {

        String filePath = "data/csv/api/luckySpin/create-new-user.csv";

        Object[][] data = CSVDataReader.getDataFromCSVFile(filePath);
        return data;
    }

    @DataProvider(name = "Verify rate of lucky spin")
    public static Object[][] initDataVerifyRateOfLuckySpin() throws Exception {

        String filePath = "data/csv/api/dashboard/user/verify-rate-of-lucky-spin.csv";

        Object[][] data = CSVDataReader.getDataFromCSVFile(filePath);
        return data;
    }
}
