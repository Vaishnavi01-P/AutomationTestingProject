package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Synchronization utility class providing Explicit Waits using ExpectedConditions.
 */
public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int defaultTimeoutSeconds;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeoutSeconds = ConfigReader.getIntProperty("timeout", 10);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeoutSeconds));
    }

    public WaitUtils(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.defaultTimeoutSeconds = timeoutSeconds;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Wait for an element to be visible by locator.
     */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for a WebElement to be visible.
     */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for all elements located by locator to be visible.
     */
    public List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait for an element to be clickable by locator.
     */
    public WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for a WebElement to be clickable.
     */
    public WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for an element to be present in DOM.
     */
    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element invisibility.
     */
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for page title to contain specific substring.
     */
    public boolean waitForTitleContains(String titleFraction) {
        return wait.until(ExpectedConditions.titleContains(titleFraction));
    }

    /**
     * Wait for page URL to contain specific substring.
     */
    public boolean waitForUrlContains(String urlFraction) {
        return wait.until(ExpectedConditions.urlContains(urlFraction));
    }

    /**
     * Wait for element text to match or contain expected string.
     */
    public boolean waitForTextToBePresent(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Wait for element selection state.
     */
    public boolean waitForElementToBeSelected(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeSelected(element));
    }
}
