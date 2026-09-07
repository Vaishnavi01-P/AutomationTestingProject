package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Thread-safe ExtentReports initialization and reporting manager.
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Initializes ExtentReports with ExtentSparkReporter and system metadata.
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.getProperty("extentReportPath", "reports/ExtentReport.html");
            File reportFile = new File(reportPath);
            File parentDir = reportFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Execution Report");
            sparkReporter.config().setReportName("Registration Application E2E Regression Suite");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // System and execution environment metadata
            extent.setSystemInfo("Application", "Demo Automation Registration");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Browser", ConfigReader.getProperty("browser", "Chrome"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
        return extent;
    }

    /**
     * Creates and attaches a new test node to the current thread.
     */
    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        extentTest.set(test);
        return test;
    }

    /**
     * Returns the ExtentTest instance for the current thread.
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Flushes and writes the report to disk.
     */
    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
