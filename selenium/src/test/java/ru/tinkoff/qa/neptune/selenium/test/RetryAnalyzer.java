package ru.tinkoff.qa.neptune.selenium.test;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private static final int RETRY_LIMIT = 3;

    private int counter = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (counter < RETRY_LIMIT) {
            counter++;
            result.setStatus(ITestResult.STARTED);
            return true;
        }
        counter = 0;
        return false;
    }
}
