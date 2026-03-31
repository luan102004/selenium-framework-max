package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX = 1; // retry 1 lần

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX) {
            count++;
            System.out.println("[Retry] Lần " + count + " cho: " + result.getName());
            return true;
        }
        return false;
    }
}
