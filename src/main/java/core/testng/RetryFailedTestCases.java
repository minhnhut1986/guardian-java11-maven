package core.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import core.Config;
import core.Log;

public class RetryFailedTestCases implements IRetryAnalyzer {

    private int retryCnt = 0;

    public boolean retry(ITestResult result) {

        if (!result.isSuccess()) {
            if (retryCnt < Config.maxRetryTest) {

                retryCnt++;

                Log.info("Retrying " + result.getName() + " again and the count is " + retryCnt);

                result.setStatus(ITestResult.FAILURE);

                return true;
            }

        } else {

            result.setStatus(ITestResult.SUCCESS);
        }

        return false;
    }

}
