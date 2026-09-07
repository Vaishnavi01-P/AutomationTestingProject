package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver factory and lifecycle manager.
 * Manages browser instances per thread for parallel and cross-browser execution.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initializes and returns a WebDriver instance based on browser configuration.
     *
     * @return WebDriver instance for current thread
     */
    public static WebDriver initDriver() {
        String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();
        boolean headless = ConfigReader.getBooleanProperty("headless", false);
        return initDriver(browser, headless);
    }

    /**
     * Initializes a specific browser with optional headless mode.
     *
     * @param browser  Target browser (chrome, firefox, edge)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver initDriver(String browser, boolean headless) {
        if (driverThreadLocal.get() != null) {
            return driverThreadLocal.get();
        }

        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "firefox":
                try {
                    WebDriverManager.firefoxdriver().setup();
                } catch (Exception e) {
                    System.out.println("WebDriverManager note: using built-in Selenium driver resolution for Firefox");
                }
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                try {
                    WebDriverManager.edgedriver().setup();
                } catch (Exception e) {
                    System.out.println("WebDriverManager note: using built-in Selenium driver resolution for Edge");
                }
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                try {
                    WebDriverManager.chromedriver().setup();
                } catch (Exception e) {
                    System.out.println("WebDriverManager note: using built-in Selenium driver resolution for Chrome");
                }
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--window-size=1920,1080");

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                }

                driver = new ChromeDriver(chromeOptions);
                break;
        }

        // Configure timeouts and window management
        int pageLoadTimeout = ConfigReader.getIntProperty("pageLoadTimeout", 30);
        int implicitWait = ConfigReader.getIntProperty("implicitWait", 0);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        if (implicitWait > 0) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        }

        if (!headless) {
            driver.manage().window().maximize();
        }

        driverThreadLocal.set(driver);
        return driver;
    }

    /**
     * Get the WebDriver instance for current thread.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quits and cleans up the WebDriver instance for current thread.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
