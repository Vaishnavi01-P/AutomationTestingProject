package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utilities.WaitUtils;

import java.util.List;

/**
 * Page Object Model representation of the Registration Page.
 * Encapsulates all page locators and user interaction workflows.
 */
public class RegisterPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;
    private final JavascriptExecutor jsExecutor;

    // --- Locators ---
    private final By formTitle = By.cssSelector("div.center h2");
    private final By registrationForm = By.id("basicBootstrapForm");

    private final By firstNameInput = By.cssSelector("input[ng-model='FirstName']");
    private final By lastNameInput = By.cssSelector("input[ng-model='LastName']");
    private final By addressTextarea = By.cssSelector("textarea[ng-model='Adress']");
    private final By emailInput = By.cssSelector("input[ng-model='EmailAdress']");
    private final By phoneInput = By.cssSelector("input[ng-model='Phone']");

    private final By maleRadio = By.cssSelector("input[value='Male']");
    private final By femaleRadio = By.cssSelector("input[value='FeMale']");

    private final By cricketCheckbox = By.id("checkbox1");
    private final By moviesCheckbox = By.id("checkbox2");
    private final By hockeyCheckbox = By.id("checkbox3");

    private final By languagesDropdown = By.id("msdd");
    private final By selectedLanguageTags = By.cssSelector("div#msdd .ui-autocomplete-multiselect-item");

    private final By skillsDropdown = By.id("Skills");
    private final By countriesDropdown = By.id("countries");
    private final By select2CountryContainer = By.cssSelector("span.select2-selection");
    private final By select2SearchField = By.cssSelector("input.select2-search__field");
    private final By select2Results = By.cssSelector("ul#select2-country-results li");

    private final By yearDropdown = By.id("yearbox");
    private final By monthDropdown = By.cssSelector("select[ng-model='monthbox']");
    private final By dayDropdown = By.id("daybox");

    private final By passwordInput = By.id("firstpassword");
    private final By confirmPasswordInput = By.id("secondpassword");
    private final By fileUploadInput = By.id("imagesrc");

    private final By submitButton = By.id("submitbtn");
    private final By refreshButton = By.id("Button1");

    private final By emailExistsError = By.xpath("//div[contains(text(),'Email already exists')]");
    private final By phoneExistsError = By.xpath("//div[contains(text(),'Phone number already exists')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        removeAdOverlays();
    }

    /**
     * Removes floating Google AdSense iframes and banner overlays that can intercept clicks.
     */
    public void removeAdOverlays() {
        try {
            jsExecutor.executeScript(
                    "var iframes = document.querySelectorAll('iframe[id^=\"aswift\"], iframe[src*=\"googleads\"], ins.adsbygoogle');" +
                    "for (var i = 0; i < iframes.length; i++) { iframes[i].style.display = 'none'; iframes[i].style.pointerEvents = 'none'; }"
            );
        } catch (Exception ignored) {
        }
    }

    /**
     * Resilient click helper that scrolls element into view and falls back to JS click
     * if an overlay or advertisement intercepts the standard click.
     */
    public void clickElement(WebElement element) {
        scrollIntoView(element);
        try {
            element.click();
        } catch (Exception e) {
            removeAdOverlays();
            jsExecutor.executeScript("arguments[0].click();", element);
        }
    }

    // --- Page Verification Methods ---

    public boolean isPageLoaded() {
        removeAdOverlays();
        return waitUtils.waitForVisibility(registrationForm).isDisplayed();
    }

    public String getPageHeading() {
        return waitUtils.waitForVisibility(formTitle).getText();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isEmailExistsErrorDisplayed() {
        try {
            return driver.findElement(emailExistsError).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPhoneExistsErrorDisplayed() {
        try {
            return driver.findElement(phoneExistsError).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areKeyFieldsDisplayed() {
        return waitUtils.waitForVisibility(firstNameInput).isDisplayed()
                && waitUtils.waitForVisibility(lastNameInput).isDisplayed()
                && waitUtils.waitForVisibility(emailInput).isDisplayed()
                && waitUtils.waitForVisibility(phoneInput).isDisplayed()
                && waitUtils.waitForVisibility(maleRadio).isDisplayed()
                && waitUtils.waitForVisibility(submitButton).isDisplayed();
    }

    // --- Actions: Personal Information ---

    public void enterFirstName(String firstName) {
        WebElement element = waitUtils.waitForVisibility(firstNameInput);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(firstName);
    }

    public String getEnteredFirstName() {
        return driver.findElement(firstNameInput).getAttribute("value");
    }

    public void enterLastName(String lastName) {
        WebElement element = waitUtils.waitForVisibility(lastNameInput);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(lastName);
    }

    public String getEnteredLastName() {
        return driver.findElement(lastNameInput).getAttribute("value");
    }

    public void enterAddress(String address) {
        WebElement element = waitUtils.waitForVisibility(addressTextarea);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(address);
    }

    public String getEnteredAddress() {
        return driver.findElement(addressTextarea).getAttribute("value");
    }

    public void enterEmail(String email) {
        WebElement element = waitUtils.waitForVisibility(emailInput);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(email);
    }

    public String getEnteredEmail() {
        return driver.findElement(emailInput).getAttribute("value");
    }

    public void enterPhone(String phone) {
        WebElement element = waitUtils.waitForVisibility(phoneInput);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(phone);
    }

    public String getEnteredPhone() {
        return driver.findElement(phoneInput).getAttribute("value");
    }

    // --- Actions: Gender ---

    public void selectGender(String gender) {
        if ("Male".equalsIgnoreCase(gender)) {
            WebElement male = waitUtils.waitForPresence(maleRadio);
            if (!male.isSelected()) {
                clickElement(male);
            }
        } else if ("Female".equalsIgnoreCase(gender) || "FeMale".equalsIgnoreCase(gender)) {
            WebElement female = waitUtils.waitForPresence(femaleRadio);
            if (!female.isSelected()) {
                clickElement(female);
            }
        }
    }

    public boolean isGenderSelected(String gender) {
        if ("Male".equalsIgnoreCase(gender)) {
            return driver.findElement(maleRadio).isSelected();
        } else {
            return driver.findElement(femaleRadio).isSelected();
        }
    }

    // --- Actions: Hobbies ---

    public void selectHobby(String hobby) {
        WebElement checkbox;
        if ("Cricket".equalsIgnoreCase(hobby)) {
            checkbox = waitUtils.waitForPresence(cricketCheckbox);
        } else if ("Movies".equalsIgnoreCase(hobby)) {
            checkbox = waitUtils.waitForPresence(moviesCheckbox);
        } else if ("Hockey".equalsIgnoreCase(hobby)) {
            checkbox = waitUtils.waitForPresence(hockeyCheckbox);
        } else {
            return;
        }

        if (!checkbox.isSelected()) {
            clickElement(checkbox);
        }
    }

    public boolean isHobbySelected(String hobby) {
        if ("Cricket".equalsIgnoreCase(hobby)) {
            return driver.findElement(cricketCheckbox).isSelected();
        } else if ("Movies".equalsIgnoreCase(hobby)) {
            return driver.findElement(moviesCheckbox).isSelected();
        } else if ("Hockey".equalsIgnoreCase(hobby)) {
            return driver.findElement(hockeyCheckbox).isSelected();
        }
        return false;
    }

    // --- Actions: Languages (Multi-select) ---

    public void selectLanguage(String languageName) {
        removeAdOverlays();
        WebElement msdd = waitUtils.waitForPresence(languagesDropdown);
        clickElement(msdd);

        By langOptionLocator = By.xpath("//ul[contains(@class,'ui-autocomplete')]//li/a[translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='" + languageName.toLowerCase() + "']");
        try {
            WebElement targetOption = waitUtils.waitForPresence(langOptionLocator);
            clickElement(targetOption);
        } catch (Exception e) {
            WebElement targetOption = driver.findElement(langOptionLocator);
            jsExecutor.executeScript("arguments[0].click();", targetOption);
        }

        // Dismiss multi-select popup
        try {
            driver.findElement(formTitle).click();
        } catch (Exception ignored) {
        }
    }

    public boolean isLanguageSelected(String languageName) {
        List<WebElement> items = driver.findElements(selectedLanguageTags);
        for (WebElement item : items) {
            if (item.getText().equalsIgnoreCase(languageName) || item.getText().contains(languageName)) {
                return true;
            }
        }
        String msddText = driver.findElement(languagesDropdown).getText();
        return msddText != null && msddText.contains(languageName);
    }

    // --- Actions: Skills ---

    public void selectSkill(String skillName) {
        WebElement element = waitUtils.waitForVisibility(skillsDropdown);
        scrollIntoView(element);
        Select select = new Select(element);
        select.selectByVisibleText(skillName);
    }

    public String getSelectedSkill() {
        Select select = new Select(driver.findElement(skillsDropdown));
        return select.getFirstSelectedOption().getText();
    }

    // --- Actions: Country ---

    public void selectCountry(String countryName) {
        WebElement element = waitUtils.waitForVisibility(countriesDropdown);
        scrollIntoView(element);
        Select select = new Select(element);
        try {
            select.selectByVisibleText(countryName);
        } catch (Exception e) {
            // If standard countries dropdown is empty, select via Select2 dropdown
            selectCountryFromSelect2(countryName);
        }
    }

    public void selectCountryFromSelect2(String countryName) {
        try {
            WebElement select2Box = waitUtils.waitForPresence(select2CountryContainer);
            clickElement(select2Box);

            WebElement searchInput = waitUtils.waitForVisibility(select2SearchField);
            searchInput.sendKeys(countryName);

            List<WebElement> results = waitUtils.waitForAllVisible(select2Results);
            for (WebElement opt : results) {
                if (opt.getText().trim().equalsIgnoreCase(countryName)) {
                    clickElement(opt);
                    return;
                }
            }
        } catch (Exception e) {
            // Fallback JS select
            jsExecutor.executeScript("var sel = document.getElementById('country'); if(sel) { sel.value='" + countryName + "'; $('#country').trigger('change'); }");
        }
    }

    public String getSelectedCountry() {
        try {
            WebElement select2Text = driver.findElement(By.id("select2-country-container"));
            if (select2Text.isDisplayed() && !select2Text.getText().isEmpty()) {
                return select2Text.getText().trim();
            }
        } catch (Exception ignored) {
        }
        Select select = new Select(driver.findElement(countriesDropdown));
        return select.getFirstSelectedOption().getText();
    }

    // --- Actions: Date of Birth ---

    public void selectDateOfBirth(String year, String month, String day) {
        if (year != null && !year.isEmpty()) {
            WebElement yElement = waitUtils.waitForVisibility(yearDropdown);
            scrollIntoView(yElement);
            new Select(yElement).selectByVisibleText(year);
        }
        if (month != null && !month.isEmpty()) {
            WebElement mElement = waitUtils.waitForVisibility(monthDropdown);
            scrollIntoView(mElement);
            new Select(mElement).selectByVisibleText(month);
        }
        if (day != null && !day.isEmpty()) {
            WebElement dElement = waitUtils.waitForVisibility(dayDropdown);
            scrollIntoView(dElement);
            new Select(dElement).selectByVisibleText(day);
        }
    }

    public String getSelectedYear() {
        return new Select(driver.findElement(yearDropdown)).getFirstSelectedOption().getText();
    }

    public String getSelectedMonth() {
        return new Select(driver.findElement(monthDropdown)).getFirstSelectedOption().getText();
    }

    public String getSelectedDay() {
        return new Select(driver.findElement(dayDropdown)).getFirstSelectedOption().getText();
    }

    // --- Actions: Password ---

    public void enterPassword(String password) {
        WebElement element = waitUtils.waitForVisibility(passwordInput);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(password);
        triggerBlur(passwordInput);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement element = waitUtils.waitForVisibility(confirmPasswordInput);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(confirmPassword);
        triggerBlur(confirmPasswordInput);
    }

    public String getEnteredPassword() {
        return driver.findElement(passwordInput).getAttribute("value");
    }

    public String getEnteredConfirmPassword() {
        return driver.findElement(confirmPasswordInput).getAttribute("value");
    }

    // --- Actions: File Upload ---

    public void uploadFile(String absoluteFilePath) {
        WebElement uploadEl = driver.findElement(fileUploadInput);
        scrollIntoView(uploadEl);
        uploadEl.sendKeys(absoluteFilePath);
    }

    public String getUploadedFileName() {
        return driver.findElement(fileUploadInput).getAttribute("value");
    }

    // --- Actions: Form Submission & Refresh ---

    public void clickSubmit() {
        WebElement submitBtn = waitUtils.waitForPresence(submitButton);
        clickElement(submitBtn);
    }

    public void clickRefresh() {
        WebElement refBtn = waitUtils.waitForPresence(refreshButton);
        clickElement(refBtn);
    }

    // --- HTML5 & Browser Validation Helpers ---

    public boolean isElementValid(By locator) {
        WebElement element = driver.findElement(locator);
        return (Boolean) jsExecutor.executeScript("return arguments[0].checkValidity();", element);
    }

    public String getElementValidationMessage(By locator) {
        WebElement element = driver.findElement(locator);
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", element);
    }

    public boolean isFirstNameValid() {
        return isElementValid(firstNameInput);
    }

    public String getFirstNameValidationMessage() {
        return getElementValidationMessage(firstNameInput);
    }

    public boolean isEmailValid() {
        return isElementValid(emailInput);
    }

    public String getEmailValidationMessage() {
        return getElementValidationMessage(emailInput);
    }

    public boolean isPhoneValid() {
        return isElementValid(phoneInput);
    }

    public String getPhoneValidationMessage() {
        return getElementValidationMessage(phoneInput);
    }

    public boolean isConfirmPasswordValid() {
        return isElementValid(confirmPasswordInput);
    }

    public String getConfirmPasswordValidationMessage() {
        return getElementValidationMessage(confirmPasswordInput);
    }

    public void triggerBlur(By locator) {
        WebElement element = driver.findElement(locator);
        jsExecutor.executeScript("arguments[0].dispatchEvent(new Event('blur'));", element);
    }

    public void scrollIntoView(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", element);
    }
}
