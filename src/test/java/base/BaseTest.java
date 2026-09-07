package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.RegisterPage;
import utilities.ConfigReader;
import utilities.DriverManager;
import utilities.ExtentReportManager;

/**
 * Base Test class handling driver initialization, URL navigation,
 * page object instantiation, suite setup, and teardown.
 */
public class BaseTest {

    protected ThreadLocal<RegisterPage> registerPageThreadLocal = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        // Initialize ExtentReports before test suite execution
        ExtentReportManager.getInstance();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = DriverManager.initDriver();
        String targetUrl = ConfigReader.getProperty("url", "https://demo.automationtesting.in/Register.html");
        driver.get(targetUrl);
        registerPageThreadLocal.set(new RegisterPage(driver));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
        registerPageThreadLocal.remove();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        ExtentReportManager.flushReports();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public RegisterPage getRegisterPage() {
        return registerPageThreadLocal.get();
    }
}
