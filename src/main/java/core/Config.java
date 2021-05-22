package core;

import java.util.Arrays;
import java.util.List;

public class Config {

    public static String globalEnvironment = "test";
    public static String platform = "api";
    public static int maxRetryTest = 0;

    public static Boolean isBrowserHeadless = null;

    public static boolean isCleanDataTest = false;

    public static void printLine() {
        System.out.println("---------------------------------------------------------------");
    }

    public static void init(String environment) {

        Log.info("Init Test");

        Config.globalEnvironment = environment;
        Log.info("Global environment : " + environment);

        String sysRetryTest = System.getProperty("retry.test");
        String sysIsBrowserHeadless = System.getProperty("is.browser.headless");
        String sysIsCleanDataTest = System.getProperty("is.clean.data.test");

        if (sysRetryTest != null) {
            maxRetryTest = Integer.valueOf(sysRetryTest);
        }

        if (sysIsBrowserHeadless != null) {
            isBrowserHeadless = Boolean.valueOf(sysIsBrowserHeadless);
        }

        if (sysIsCleanDataTest != null) {
            isCleanDataTest = Boolean.valueOf(sysIsCleanDataTest);
        }

        Log.info("Retry test : " + maxRetryTest);
        Log.info("Is browser headless : " + isBrowserHeadless);
        Log.info("Is clean data test : " + isCleanDataTest);

        printLine();
    }
}
