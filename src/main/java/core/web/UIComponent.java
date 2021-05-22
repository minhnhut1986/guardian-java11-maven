package core.web;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import core.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UIComponent {

    public static void reloadPage(WebDriver driver){
        Log.debug("Reload page");
        driver.navigate().refresh();
    }

    public static void navigateToUrl(WebDriver driver, String url) {
        Log.debug("Navigate to " + url);
        driver.navigate().to(url);
        reloadPage(driver);
        Timer.jsWaitForPageToLoad(driver);
    }

    public static void switchTab(WebDriver driver, int tabIndex){

        Log.debug("Switch to tab " + tabIndex);
        ArrayList<String> windowTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(windowTabs.get(tabIndex));
    }

    public static void switchTab(WebDriver driver, String title){
        Set<String> windows = driver.getWindowHandles();
        Log.debug("Number of tab : " + windows.size() + ", need to switch to : " + title);
        for (String window : windows) {
            Log.debug("In checking tab with name : " + window);
            driver.switchTo().window(window);
            if (driver.getTitle().contains(title)) {
                return;
            }
        }
    }

    public static void switchToFinalTab(WebDriver driver){

        Log.debug("Switch to final tab");
        ArrayList<String> windowTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(windowTabs.get(windowTabs.size() - 1));

        Timer.jsWaitForPageToLoad(driver);
    }

    public static void removeFinalTab(WebDriver driver){

        ArrayList<String> windowTabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(windowTabs.get(windowTabs.size() - 1));
        driver.close();
    }

    public static void removeTab(WebDriver driver, String tabName){
        try{
            Log.debug("Tab that need to remove : " + tabName);
            ArrayList<String> windowTabs = new ArrayList<String>(driver.getWindowHandles());
            for(String tab : windowTabs){
                Log.debug("Tab : " + tab);
            }
            driver.switchTo().window(tabName);
            driver.close();
            driver.switchTo().window(windowTabs.get(1));

        } catch(Exception ex){
            Log.errorAndStop("Have error when remove first tab : " + ex.getMessage());
        }

    }

    public static boolean selectDropDownBoxItemByIndex(WebDriver driver, WebElement element,
                                                       int index) {
        try {
            Timer.waitForVisible(driver, element);
            Select dropDownSelecter = new Select(element);
            dropDownSelecter.selectByIndex(index);
            return true;
        } catch (Exception ex) {
            Log.errorAndStop("ERROR : have exception in selectDropDownBoxItemByIndex" +
                    " funtion with WebElement : "
                    + element.getAttribute("id")
                    + " / Error msg : "
                    + ex.getMessage());
            return false;
        }

    }

    public static boolean selectDropDownBoxItemByVisibleText(WebDriver driver, WebElement element,
                                                             String visibleText) {
        Log.debug("Select dropdown box item by visible text : " + visibleText);
        try {
            Select dropDownSelector = new Select(element);
            dropDownSelector.selectByVisibleText(visibleText);
            return true;
        } catch (Exception ex) {

            Log.errorAndStop("ERROR : have exception in selectDropDownBoxItemByText" +
                    " function with WebElement : "
                    + element.getAttribute("id")
                    + " / Error msg : "
                    + ex.getMessage());
            return false;
        }
    }

    public static boolean selectDropDownBoxItemByValue(WebDriver driver, WebElement element,
                                                             String value) {
        Log.debug("Select dropdown box item by value : " + value);
        try {
            Select dropDownSelector = new Select(element);
            dropDownSelector.selectByValue(value);
            return true;
        } catch (Exception ex) {
            Log.errorAndStop("ERROR : have exception in selectDropDownBoxItemByText" +
                    " function with WebElement : "
                    + element.getAttribute("id")
                    + " / Error msg : "
                    + ex.getMessage());
            return false;
        }
    }

    public static boolean selectSuggestionItem(WebDriver driver,
                                               String parentDivId) {
        try {

            List<WebElement> suggestionList = driver.findElements(By
                    .xpath("//div[@id='" + parentDivId + "']/div/ul/li"));
            Log.debug("Number of suggestion : " + +suggestionList.size());
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By
                    .id(parentDivId)));
            for (WebElement suggestion : suggestionList) {
                Log.debug("Choose suggestion : " + suggestion.getText());
                suggestion.click();
                break;
            }
            return true;
        } catch (Exception ex) {
            Log.errorAndStop("ERROR : have exception in selectSuggestionItem function " +
                    "with parentDivId : "
                    + parentDivId + " / Error msg : " + ex.getMessage());
            return false;
        }

    }

    public static void clearText(WebDriver driver, WebElement element){

        Log.debug("Clear text in web element");
        try{
            Timer.waitForVisible(driver, element);
            element.clear();
        } catch(Exception ex){

            Log.errorAndStop("Have error when clear text : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());
        }
    }

    public static void fillText(WebDriver driver, WebElement element, String text ){

        Log.debug("Fill : " + text + " in web element");
        try{
            Timer.waitForVisible(driver, element);
            element.clear();
            element.sendKeys(text);
        } catch(Exception ex1){

            Log.debug("Using action to fill text");
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(element);
                actions.click();
                actions.sendKeys(text);
                actions.build().perform();
            } catch (Exception ex2) {

                Log.errorAndStop("Have error when input : " + text + " into : "
                        + element.getAttribute("id") + " | Error message : "
                        + ex2.getMessage());
            }
        }
    }

    public static void fillTextOnDropdownBox(WebDriver driver, WebElement element, String text ){

        Log.debug("Fill : " + text + " in web element");
        try {
            Timer.waitForVisible(driver, element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.click();
            actions.sendKeys(text);
            actions.build().perform();
        } catch (Exception ex) {

                Log.errorAndStop("Have error when input : " + text + " into : "
                        + element.getAttribute("id") + " | Error message : "
                        + ex.getMessage());
        }
    }

    public static void fillTextByAction(WebDriver driver, String text ){

        Log.debug("Fill : " + text + " in web element");
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(text);
            actions.build().perform();
        } catch (Exception ex) {

            Log.errorAndStop("Have error when input : " + text + ex.getMessage());
        }
    }

    public static void uploadImage(WebElement element, String imagePath ){

        Log.debug("Upload image with path : " + imagePath + " in web element");
        try{
            element.sendKeys(imagePath);
        } catch(Exception ex){

            Log.errorAndStop("Have error when upload image with path : " + imagePath + " into : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());
        }
    }

    public static void fillTextWithoutClear(WebDriver driver, WebElement element, String text ){

        Log.debug("Fill : " + text + " in web element");
        try{
            Timer.waitForVisible(driver, element);
            element.sendKeys(text);
        } catch(Exception ex1){

            Log.debug("Using action to fill text");
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(element);
                actions.click();
                actions.sendKeys(text);
                actions.build().perform();
            } catch (Exception ex2) {

                Log.errorAndStop("Have error when input : " + text + " into : "
                        + element.getAttribute("id") + " | Error message : "
                        + ex2.getMessage());
            }
        }
    }

    public static void pressKeyboardKeys(WebDriver driver, WebElement element, CharSequence charSequence){

        Log.debug("Press keyboard keys in web element");
        try{
            Timer.waitForVisible(driver, element);
            element.sendKeys(charSequence);
        } catch(Exception ex1){

            Log.debug("Using action to press keyboard");
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(element);
                actions.click();
                actions.sendKeys(charSequence);
                actions.build().perform();
            } catch (Exception ex2) {

                Log.errorAndStop("Have error when press keyboard keys |"
                        + element.getAttribute("id") + " | Error message : "
                        + ex2.getMessage());
            }
        }
    }

    public static void pressKeyboardKeys(WebDriver driver, CharSequence charSequence){

        Log.debug("Press keyboard keys");

        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(charSequence);
            actions.build().perform();
        } catch (Exception ex) {

            Log.errorAndStop("Have error when press keyboard keys |"
                    + " | Error message : " + ex.getMessage());
        }
    }

    public static void clickOnElement(WebDriver driver, WebElement element) {

        Log.debug("Click on web element");
        try {
            Timer.waitForVisible(driver, element);
            element.click();

        } catch (Exception ex1){

            Log.debug("Using javascript executor to click element");
            try {
                JavascriptExecutor je = (JavascriptExecutor)driver;
                je.executeScript("arguments[0].click();", element);

            } catch (Exception ex2){

                Log.errorAndStop("Have error when click : "
                        + element.getAttribute("id") + " | Error message : "
                        + ex2.getMessage());
            }

        }
    }

    public static void clickOnElementByJS(WebDriver driver, WebElement element) {

        Log.debug("Using javascript executor to click element");
        try {
            JavascriptExecutor je = (JavascriptExecutor)driver;
            je.executeScript("arguments[0].click();", element);

        } catch (Exception ex){

            Log.errorAndStop("Have error when click : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());
        }
    }

    public static void clickOnDropdownBox(WebDriver driver, WebElement element){

        Log.debug("Click on dropdown box");
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.click();
            actions.build().perform();
        } catch (Exception ex) {

            Log.errorAndStop("Have error when click on web element : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());
        }
    }

    public static void clickOnDropdownBoxItem(WebDriver driver, WebElement element, String dbItemValue ){

        Log.debug("Click on dropdown box item with value : " + dbItemValue);
        try {
            Timer.waitForVisible(driver, element);

            WebElement eItem = element.findElement(By.xpath("./li[text()='" + dbItemValue + "']"));
//            scrollIntoView(driver, eItem);
            clickOnElement(driver, eItem);
        } catch (Exception ex) {

            Log.errorAndStop("Have error when click on "
                    + element.getAttribute("id") + " with value : " + dbItemValue
                    + " | Error message : " + ex.getMessage());
        }
        Timer.waiting(1);
    }

    public static void clickOnDropdownBoxItemOnKYCPage(WebDriver driver, WebElement element, String dbItemValue ){

        Log.debug("Click on dropdown box item with value : " + dbItemValue);
        try {
            Timer.waitForVisible(driver, element);

            WebElement eItem = element.findElement(By.xpath("./li[text()='" + dbItemValue + "']"));
//            scrollIntoView(driver, eItem);
            clickOnElement(driver, eItem);
        } catch (Exception ex) {

            Log.errorAndStop("Have error when click on "
                    + element.getAttribute("id") + " with value : " + dbItemValue
                    + " | Error message : " + ex.getMessage());
        }
        Timer.waiting(1);
    }

    public static void doubleClickElement(WebDriver driver, WebElement element) {

        Log.debug("Double click on web element");

        Actions actions = new Actions(driver);

        try {
            Timer.waitForVisible(driver, element);
            actions.doubleClick(element).perform();

        } catch (Exception ex){

            Log.errorAndStop("Have error when double click : "
                    + element.getAttribute("name") + " | Error message : "
                    + ex.getMessage());
        }
    }

    // context click = right click
    public static void rightClickElement(WebDriver driver, WebElement element) {

        Log.debug("Right click on web element");

        Actions actions = new Actions(driver);

        try {
            Timer.waitForVisible(driver, element);
            actions.contextClick(element).perform();

        } catch (Exception ex){

            Log.errorAndStop("Have error when right click : "
                    + element.getAttribute("name") + " | Error message : "
                    + ex.getMessage());
        }
    }

    public static String getElementInnerText(WebDriver driver, WebElement element) {
        try {
            Timer.waitForVisible(driver, element);

            String elementInnerText = element.getText();
//            Log.debug("Element inner text : " + elementInnerText);
            return elementInnerText;

        } catch(Exception ex){
            Log.errorAndStop("Have error when get inner text : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());

            return null;
        }
    }

    public static String getElementInnerTextByJs(WebDriver driver, WebElement element) {
        try {
            Timer.waitForVisible(driver, element);

            JavascriptExecutor je = (JavascriptExecutor)driver;
            String elementInnerText = (String) je.executeScript("return arguments[0].value;", element);

            Log.debug("Element inner text : " + elementInnerText);
            return elementInnerText;

        } catch(Exception ex){
            Log.errorAndStop("Have error when get inner text : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());

            return null;
        }
    }

    public static String getElementAttribute(WebDriver driver, WebElement element, String attribute) {
        List<WebElement> listElement = Arrays.asList(element);
        try {
            Timer.waitForListElementExist(driver, listElement);

            String elementAttributeValue = element.getAttribute(attribute);
            Log.debug("Element attribute '" + attribute + "' value : " + elementAttributeValue);
            return elementAttributeValue;

        } catch(Exception ex){
            Log.errorAndStop("Have error when get attribute " + attribute + " : "
                    + element.getAttribute("id") + " | Error message : "
                    + ex.getMessage());

            return null;
        }
    }

    public static boolean isListElementExist(WebDriver driver, List<WebElement> listElement) {
        try {
            Timer.waitForListElementExist(driver, listElement);
            return true;

        } catch(Exception ex){
            Log.errorAndStop("List element is not exist : "
                    + ex.getMessage());

            return false;
        }
    }

    public static WebElement getFirstOptionFromSelect( WebElement selectElement){
        Select select = new Select(selectElement);
        WebElement option = select.getFirstSelectedOption();

        return option;
    }

    public static void scrollIntoView(WebDriver driver, WebElement webElement){

        JavascriptExecutor je = (JavascriptExecutor)driver;
        je.executeScript("arguments[0].scrollIntoView();", new Object[]{webElement});
    }

    public static void fillTextOnAlertBox(WebDriver driver, String alertBoxText){
        Log.debug("Fill text on alert box");

        new WebDriverWait(driver, Timer.waitElementTime).until(ExpectedConditions.alertIsPresent());

        try {
            Alert alert = driver.switchTo().alert();

            alert.sendKeys(alertBoxText);

            alert.accept();
        } catch (NoAlertPresentException e) {
            Log.error("There is no alert: " + e.toString());
        }
    }

    public static boolean isDisable(WebElement element) {

        // If element is disabled => return "true"
        return  Boolean.valueOf(element.getAttribute("disabled"));
    }

    public static void backPreviousPage( WebDriver driver){
        driver.navigate().back();
    }
}
