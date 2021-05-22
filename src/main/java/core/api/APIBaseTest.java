package core.api;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import core.Config;
import core.Log;
import core.PropertyValues;

public class APIBaseTest {

    public static PropertyValues propertyValues;
    public static String luckySpinHost;
    public static String qcReportHost;


    @BeforeSuite(alwaysRun=true)
    @Parameters( {"environment", "platform"} )
    public void setUp(String environment, String platform) {

        Config.init(environment);

        String propFilePath = "config/" + platform + ".properties";
        Log.debug("API properties file path : " + propFilePath);
        propertyValues = new PropertyValues(propFilePath);
        luckySpinHost = API.getLuckySpinHostAPI(propertyValues);
        qcReportHost = API.getQcReportHostAPI(propertyValues);
    }

    @AfterSuite(alwaysRun=true)
    public void generateHTMLReports() {
//        ReportHelper.generateCucumberReport();
    }

    @AfterSuite(alwaysRun=true)
    public void quitTest() {
        propertyValues.closeInit();
    }
}
