package core.web;

import com.google.common.io.Files;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import core.Config;
import core.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SeleniumWebHelper {

    public static void  configureDriverPath() {
        if(System.getProperty("os.name").startsWith("Linux")) {
            String firefoxDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/linux/geckodriver";
            System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
            String chromeDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/linux/chromedriver";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }
        if(System.getProperty("os.name").startsWith("Mac")) {
            String firefoxDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/geckodriver";
            System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
            String chromeDriverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/mac/chromedriver";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }
        if(System.getProperty("os.name").startsWith("Windows")) {
            String firefoxDriverPath = System.getProperty("user.dir") + "//src//test//resources//drivers//windows//geckodriver.exe";
            System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
            String chromeDriverPath = System.getProperty("user.dir") + "//src//test//resources//drivers//windows//chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }
    }

    public static WebDriver initBrowser(boolean isHeadless, String browserType) {

        WebDriver driver = null;

        configureDriverPath();

        if (browserType.equals("firefox")) {
            driver = new FirefoxDriver();
        } else if (browserType.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);

            if (Config.isBrowserHeadless != null) {
                isHeadless = Config.isBrowserHeadless;
            }

            if (isHeadless) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-dev-shm-usage");
            }

            options.addArguments("--no-sandbox");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-notifications");
            options.setExperimentalOption("useAutomationExtension", false);
            driver = new ChromeDriver(options);
        }

        return driver;
    }

    public static void maximizeWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    public static void deleteAllCookies(WebDriver driver) {
        driver.manage().deleteAllCookies();
    }

    public static void takeScreenshot(WebDriver driver, File screenShotFile) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        screenShotFile.getParentFile().mkdir();
        screenShotFile.createNewFile();
        Files.copy(scrFile, screenShotFile);
    }

    public static void takeScreenshot(Scenario scenario,
                                      WebDriver driver, File screenShotFile) {
        final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", screenShotFile.getName());
        try {
            OutputStream os = new FileOutputStream(screenShotFile);

            os.write(screenshot);
            Log.debug("Successfully save screenshot");

            os.close();
        } catch (Exception ex) {
            Log.error("Exception : " + ex);
        }
    }
}
