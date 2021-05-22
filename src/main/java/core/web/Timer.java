package core.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import core.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Timer {

    static int waitPageLoadTimeOut = 10;
    static int waitElementTime = 5;

    public static void waiting(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (Exception exception) {
            Log.errorAndStop("Have error when sleeping: " + exception);
        }
    }

    public static void implicitWait(WebDriver driver, int time) {
        driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    public static void waitForVisible(WebDriver driver, WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, waitElementTime);
        wait.until(ExpectedConditions.visibilityOf(element));
//        Log.debug("Element that you're waiting for : " + element.getAttribute
//                ("id") + " is visible !");
    }

    public static void waitForClickable(WebDriver driver, WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, waitElementTime);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Log.debug("Element that you're waiting for : " + element.getAttribute
                ("id") + " is clickable !");
    }

    public static void waitForListElementExist(WebDriver driver,
                                               List<WebElement> listElement) {
        Log.debug("Wait for elements exist in : " + waitElementTime + "s !");
        (new WebDriverWait(driver, waitElementTime)).until(new ExpectedCondition
                <Boolean>() {
            public Boolean apply(WebDriver driver) {
                Log.debug("Wait for load list elements");
                if (listElement.size() > 0) {
                    Log.debug("Found list elements ! List elements size : " + listElement.size());
                    return true;
                } else {
                    Log.debug("List elements size : " + listElement.size());
                    return false;
                }
            }
        });
    }

    public static void jsWaitForPageToLoad(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        String jsCommand = "return document.readyState";
        if (!js.executeScript(jsCommand, new Object[0]).toString().equals("complete")) {
            for(int i = 0; i < waitPageLoadTimeOut; ++i) {
                waiting(2);
                if (js.executeScript(jsCommand, new Object[0]).toString().equals("complete")) {
                    break;
                }
            }
        }
    }

    public static void pageLoadWait(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(waitPageLoadTimeOut, TimeUnit.SECONDS);
    }

    public static void waitForPageTitleContains( WebDriver driver, final String startTitle){
        Log.debug("Wait for page with title contains : '" + startTitle + "' to be load in : "
                + waitPageLoadTimeOut + "s !" );
        (new WebDriverWait(driver, waitPageLoadTimeOut)).until(new ExpectedCondition
                <Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith(startTitle
                        .toLowerCase());
            }
        });
        Log.debug("Page with title contains : '" + startTitle + "' has been loaded ! ");
    }

    public static void waitForPageTitleEquals( WebDriver driver, final String title){
        Log.debug("Wait for page with title equals : '" + title + "' to be load in : "
                + waitPageLoadTimeOut + "s !" );
        (new WebDriverWait(driver, waitPageLoadTimeOut)).until(new ExpectedCondition
                <Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().equals(title
                        .toLowerCase());
            }
        });
        Log.debug("Page with title equals : '" + title + "' has been loaded ! ");
    }

    public static boolean waitForPageUrlContainsUrl(WebDriver driver, final String url){

        try {
            (new WebDriverWait(driver, waitPageLoadTimeOut)).until(new ExpectedCondition
                    <Boolean>() {
                public Boolean apply(WebDriver d) {
                    Log.debug("Current url : " + d.getCurrentUrl());
                    return d.getCurrentUrl().toLowerCase().contains(url
                            .toLowerCase());
                }
            });
            Log.debug("Page load with url : " + url);

            return true;
        } catch (Exception ex) {
            Log.error("Current page url does not contain " + url);
            return false;
        }
    }

    public static boolean waitForPageUrlContainsUrlAndStop(WebDriver driver, final String url){

        try {
            (new WebDriverWait(driver, waitPageLoadTimeOut)).until(new ExpectedCondition
                    <Boolean>() {
                public Boolean apply(WebDriver d) {
                    Log.debug("Current url : " + d.getCurrentUrl());
                    return d.getCurrentUrl().toLowerCase().contains(url
                            .toLowerCase());
                }
            });
            Log.debug("Page load with url : " + url);

            return true;
        } catch (Exception ex) {
            Log.errorAndStop("Current page url does not contain " + url);
            return false;
        }
    }

    public static boolean waitForPageUrlEqualUrl(WebDriver driver, final String url){

        try {
            (new WebDriverWait(driver, waitPageLoadTimeOut)).until(new ExpectedCondition
                    <Boolean>() {
                public Boolean apply(WebDriver d) {
                    Log.debug("Current url : " + d.getCurrentUrl());
                    return d.getCurrentUrl().toLowerCase().equals(url
                            .toLowerCase());
                }
            });
            Log.debug("Page load with url : " + url);

            return true;
        } catch (Exception ex) {
            Log.error("Current page url is not " + url);
            return false;
        }
    }

    public static boolean waitForPageUrlEqualUrlAndStop(WebDriver driver, final String url){

        try {
            (new WebDriverWait(driver, waitPageLoadTimeOut)).until(new ExpectedCondition
                    <Boolean>() {
                public Boolean apply(WebDriver d) {
                    Log.debug("Current url : " + d.getCurrentUrl());
                    return d.getCurrentUrl().toLowerCase().equals(url
                            .toLowerCase());
                }
            });
            Log.debug("Page load with url : " + url);

            return true;
        } catch (Exception ex) {
            Log.errorAndStop("Current page url is not " + url);
            return false;
        }
    }
}
