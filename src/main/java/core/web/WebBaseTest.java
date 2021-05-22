package core.web;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import core.Config;
import core.Log;
import core.PropertyValues;


public class WebBaseTest extends AbstractTestNGCucumberTests {

    public static PropertyValues propertyValues;
    public static WebDriver driver;

    int implicitWaitTime = 10;

    @BeforeSuite(alwaysRun = true)
    @Parameters( {"environment", "platform", "isHeadless", "browserType"} )
    public void setUp(String environment, String platform, String isHeadless, String browserType) {

        Config.init(environment);

        String propFilePath = "config/" + platform + ".properties";
        Log.debug("Web properties file path : " + propFilePath);
        propertyValues = new PropertyValues(propFilePath);

        driver = SeleniumWebHelper.initBrowser(Boolean.valueOf(isHeadless), browserType);

        Timer.implicitWait(driver, implicitWaitTime);

        SeleniumWebHelper.deleteAllCookies(driver);
    }

    @AfterSuite(alwaysRun=true)
    public void generateHTMLReports() {
//        ReportHelper.generateCucumberReport();
    }

    @AfterSuite(alwaysRun = true)
    public void quitTest(){
        driver.quit();
        propertyValues.closeInit();
    }
}
