package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility to capture and store screenshots during test execution and failure handling.
 */
public class ScreenshotUtils {

    private static final String DEFAULT_SCREENSHOT_DIR = "screenshots/";

    /**
     * Captures a screenshot to disk with test name and timestamp.
     *
     * @param driver   WebDriver instance
     * @param testName Name of the test method or identifier
     * @return Path to the saved screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            System.err.println("Cannot capture screenshot: WebDriver instance is null.");
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String dirPath = ConfigReader.getProperty("screenshotPath", DEFAULT_SCREENSHOT_DIR);
        File screenshotDir = new File(dirPath);
        if (!screenshotDir.exists()) {
            boolean created = screenshotDir.mkdirs();
            if (!created && !screenshotDir.exists()) {
                System.err.println("Failed to create screenshot directory: " + dirPath);
            }
        }

        String fileName = testName.replaceAll("[^a-zA-Z0-9._-]", "_") + "_" + timestamp + ".png";
        File destinationFile = new File(screenshotDir, fileName);

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File sourceFile = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, destinationFile);
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Exception while taking screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Captures a Base64 string of the current screen for direct embedding in ExtentReports.
     *
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot string
     */
    public static String captureBase64Screenshot(WebDriver driver) {
        if (driver == null) return null;
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Exception while taking Base64 screenshot: " + e.getMessage());
            return null;
        }
    }
}
