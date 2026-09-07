package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.DriverManager;
import utilities.ExtentReportManager;
import utilities.ScreenshotUtils;

/**
 * TestNG Listener implementing automated logging, screenshot capture on failure,
 * and ExtentReports integration.
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("==================================================");
        System.out.println("Starting Test Suite: " + context.getName());
        System.out.println("==================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        System.out.println("[TEST STARTING] " + testName + " - " + description);
        ExtentReportManager.createTest(testName, description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("[TEST PASSED] " + testName);
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully: " + testName);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.err.println("[TEST FAILED] " + testName + " Reason: " + result.getThrowable());

        WebDriver driver = DriverManager.getDriver();
        ExtentTest test = ExtentReportManager.getTest();

        if (driver != null) {
            // Save screenshot file to screenshots/
            String screenshotFilePath = ScreenshotUtils.captureScreenshot(driver, testName);
            String base64Screenshot = ScreenshotUtils.captureBase64Screenshot(driver);

            if (test != null) {
                test.log(Status.FAIL, "Test failed: " + result.getThrowable());
                if (base64Screenshot != null) {
                    test.fail("Failure Screenshot",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot").build());
                } else if (screenshotFilePath != null) {
                    test.fail("Failure Screenshot Path: " + screenshotFilePath);
                }
            }
        } else {
            if (test != null) {
                test.log(Status.FAIL, "Test failed (WebDriver was null): " + result.getThrowable());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("[TEST SKIPPED] " + testName);
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("==================================================");
        System.out.println("Finished Test Suite: " + context.getName());
        System.out.println("Passed: " + context.getPassedTests().size() + " | Failed: " + context.getFailedTests().size() + " | Skipped: " + context.getSkippedTests().size());
        System.out.println("==================================================");
        ExtentReportManager.flushReports();
    }
}
