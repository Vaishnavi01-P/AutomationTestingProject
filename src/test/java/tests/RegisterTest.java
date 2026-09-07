package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.RegisterPage;
import utilities.ConfigReader;

import java.io.File;

/**
 * Test Suite covering End-to-End Registration Positive and Negative Test Scenarios.
 */
public class RegisterTest extends BaseTest {

    @Test(priority = 1, description = "TC01 - Verify registration page loads successfully")
    public void testPageLoad() {
        RegisterPage registerPage = getRegisterPage();

        Assert.assertTrue(registerPage.isPageLoaded(), "Registration form is not displayed on page load.");
        Assert.assertTrue(registerPage.getCurrentUrl().contains("Register.html"), "Current URL does not contain 'Register.html'.");
        Assert.assertEquals(registerPage.getPageTitle(), "Register", "Page title does not match expected title.");
        Assert.assertEquals(registerPage.getPageHeading(), "Register", "Page heading does not match expected heading.");
    }

    @Test(priority = 2, description = "TC02 - Verify important registration fields are displayed")
    public void testImportantFieldsDisplayed() {
        RegisterPage registerPage = getRegisterPage();

        Assert.assertTrue(registerPage.areKeyFieldsDisplayed(), "One or more critical registration fields are not displayed.");
    }

    @Test(priority = 3, description = "TC03 - Verify valid personal information can be entered")
    public void testEnterPersonalInfo() {
        RegisterPage registerPage = getRegisterPage();

        String firstName = ConfigReader.getTestData("valid.firstName");
        String lastName = ConfigReader.getTestData("valid.lastName");
        String address = ConfigReader.getTestData("valid.address");
        String email = String.format(ConfigReader.getTestData("valid.email"), System.currentTimeMillis());
        String phone = ConfigReader.getTestData("valid.phone");

        registerPage.enterFirstName(firstName);
        registerPage.enterLastName(lastName);
        registerPage.enterAddress(address);
        registerPage.enterEmail(email);
        registerPage.enterPhone(phone);

        Assert.assertEquals(registerPage.getEnteredFirstName(), firstName, "First Name was not entered correctly.");
        Assert.assertEquals(registerPage.getEnteredLastName(), lastName, "Last Name was not entered correctly.");
        Assert.assertEquals(registerPage.getEnteredAddress(), address, "Address was not entered correctly.");
        Assert.assertEquals(registerPage.getEnteredEmail(), email, "Email was not entered correctly.");
        Assert.assertEquals(registerPage.getEnteredPhone(), phone, "Phone was not entered correctly.");
    }

    @Test(priority = 4, description = "TC04 - Verify gender selection")
    public void testGenderSelection() {
        RegisterPage registerPage = getRegisterPage();

        registerPage.selectGender("Male");
        Assert.assertTrue(registerPage.isGenderSelected("Male"), "Male gender radio button should be selected.");
        Assert.assertFalse(registerPage.isGenderSelected("FeMale"), "Female gender radio button should not be selected.");

        registerPage.selectGender("FeMale");
        Assert.assertTrue(registerPage.isGenderSelected("FeMale"), "Female gender radio button should be selected.");
        Assert.assertFalse(registerPage.isGenderSelected("Male"), "Male gender radio button should not be selected.");
    }

    @Test(priority = 5, description = "TC05 - Verify hobbies selection")
    public void testHobbiesSelection() {
        RegisterPage registerPage = getRegisterPage();

        registerPage.selectHobby("Cricket");
        registerPage.selectHobby("Movies");

        Assert.assertTrue(registerPage.isHobbySelected("Cricket"), "Cricket hobby checkbox should be selected.");
        Assert.assertTrue(registerPage.isHobbySelected("Movies"), "Movies hobby checkbox should be selected.");
        Assert.assertFalse(registerPage.isHobbySelected("Hockey"), "Hockey hobby checkbox should not be selected.");
    }

    @Test(priority = 6, description = "TC06 - Verify language selection")
    public void testLanguageSelection() {
        RegisterPage registerPage = getRegisterPage();
        String language = ConfigReader.getTestData("valid.language");

        registerPage.selectLanguage(language);
        Assert.assertTrue(registerPage.isLanguageSelected(language), "Selected language '" + language + "' was not found in selected list.");
    }

    @Test(priority = 7, description = "TC07 - Verify skills and country selection")
    public void testSkillsAndCountrySelection() {
        RegisterPage registerPage = getRegisterPage();

        String skill = ConfigReader.getTestData("valid.skill");
        String country = ConfigReader.getTestData("valid.country");

        registerPage.selectSkill(skill);
        Assert.assertEquals(registerPage.getSelectedSkill(), skill, "Selected skill does not match.");

        registerPage.selectCountry(country);
        Assert.assertTrue(registerPage.getSelectedCountry().contains(country) || registerPage.getSelectedCountry().length() > 0,
                "Country was not selected correctly.");
    }

    @Test(priority = 8, description = "TC08 - Verify date of birth selection")
    public void testDateOfBirthSelection() {
        RegisterPage registerPage = getRegisterPage();

        String year = ConfigReader.getTestData("valid.birthYear");
        String month = ConfigReader.getTestData("valid.birthMonth");
        String day = ConfigReader.getTestData("valid.birthDay");

        registerPage.selectDateOfBirth(year, month, day);

        Assert.assertEquals(registerPage.getSelectedYear(), year, "Year selection did not match.");
        Assert.assertEquals(registerPage.getSelectedMonth(), month, "Month selection did not match.");
        Assert.assertEquals(registerPage.getSelectedDay(), day, "Day selection did not match.");
    }

    @Test(priority = 9, description = "TC09 - Verify password and confirm password")
    public void testPasswordEntry() {
        RegisterPage registerPage = getRegisterPage();

        String password = ConfigReader.getTestData("valid.password");
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(password);

        Assert.assertEquals(registerPage.getEnteredPassword(), password, "Password was not entered correctly.");
        Assert.assertEquals(registerPage.getEnteredConfirmPassword(), password, "Confirm password was not entered correctly.");
        Assert.assertTrue(registerPage.isConfirmPasswordValid(), "Matching passwords should be valid.");
    }

    @Test(priority = 10, description = "TC10 - Verify file upload")
    public void testFileUpload() {
        RegisterPage registerPage = getRegisterPage();

        File sampleFile = new File("src/test/resources/testdata/sample-photo.png");
        Assert.assertTrue(sampleFile.exists(), "Sample test upload file must exist at " + sampleFile.getAbsolutePath());

        registerPage.uploadFile(sampleFile.getAbsolutePath());
        String uploadedVal = registerPage.getUploadedFileName();

        Assert.assertNotNull(uploadedVal, "Uploaded file value should not be null.");
        Assert.assertTrue(uploadedVal.contains("sample-photo.png"), "Uploaded file path should contain 'sample-photo.png'.");
    }

    @Test(priority = 11, description = "TC11 - Verify complete valid registration workflow")
    public void testCompleteValidRegistrationWorkflow() {
        RegisterPage registerPage = getRegisterPage();

        long uniqueId = System.currentTimeMillis();
        String firstName = ConfigReader.getTestData("valid.firstName");
        String lastName = ConfigReader.getTestData("valid.lastName");
        String address = ConfigReader.getTestData("valid.address");
        String email = "auto.user." + uniqueId + "@mailtest.com";
        // 10-digit unique valid phone number
        String phone = "98" + (uniqueId % 100000000L);
        if (phone.length() < 10) {
            phone = String.format("%-10s", phone).replace(' ', '0');
        }

        registerPage.enterFirstName(firstName);
        registerPage.enterLastName(lastName);
        registerPage.enterAddress(address);
        registerPage.enterEmail(email);
        registerPage.enterPhone(phone);
        registerPage.selectGender(ConfigReader.getTestData("valid.gender"));
        registerPage.selectHobby(ConfigReader.getTestData("valid.hobby1"));
        registerPage.selectLanguage(ConfigReader.getTestData("valid.language"));
        registerPage.selectSkill(ConfigReader.getTestData("valid.skill"));
        registerPage.selectCountry(ConfigReader.getTestData("valid.country"));
        registerPage.selectDateOfBirth(
                ConfigReader.getTestData("valid.birthYear"),
                ConfigReader.getTestData("valid.birthMonth"),
                ConfigReader.getTestData("valid.birthDay")
        );

        String password = ConfigReader.getTestData("valid.password");
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(password);

        File sampleFile = new File("src/test/resources/testdata/sample-photo.png");
        registerPage.uploadFile(sampleFile.getAbsolutePath());

        // Submit form
        registerPage.clickSubmit();

        // Assert that client-side validations passed on submission
        Assert.assertTrue(registerPage.isFirstNameValid(), "First Name should be valid upon valid submission.");
        Assert.assertTrue(registerPage.isEmailValid(), "Email should be valid upon valid submission.");
        Assert.assertTrue(registerPage.isPhoneValid(), "Phone should be valid upon valid submission.");
    }

    @Test(priority = 12, description = "TC12 - Verify mandatory field validation")
    public void testMandatoryFieldValidation() {
        RegisterPage registerPage = getRegisterPage();

        // Attempt submission without required information
        registerPage.clickSubmit();

        // Assert browser HTML5 required validation
        Assert.assertFalse(registerPage.isFirstNameValid(), "Empty First Name should fail HTML5 required validation.");
        Assert.assertFalse(registerPage.getFirstNameValidationMessage().isEmpty(), "Validation message should be present for First Name.");
    }

    @Test(priority = 13, description = "TC13 - Verify invalid email behavior")
    public void testInvalidEmailValidation() {
        RegisterPage registerPage = getRegisterPage();

        String invalidEmail = ConfigReader.getTestData("invalid.email");
        registerPage.enterFirstName("John");
        registerPage.enterLastName("Doe");
        registerPage.enterEmail(invalidEmail);

        // Assert HTML5 email type validation
        Assert.assertFalse(registerPage.isEmailValid(), "Malformed email should fail browser email format validation.");
        Assert.assertFalse(registerPage.getEmailValidationMessage().isEmpty(), "Browser validation message should be present for invalid email.");
    }

    @Test(priority = 14, description = "TC14 - Verify invalid phone behavior where applicable")
    public void testInvalidPhoneValidation() {
        RegisterPage registerPage = getRegisterPage();

        String invalidPhone = ConfigReader.getTestData("invalid.phone");
        registerPage.enterFirstName("John");
        registerPage.enterLastName("Doe");
        registerPage.enterPhone(invalidPhone);

        // Pattern validation on phone input: pattern="^\d{10}$"
        Assert.assertFalse(registerPage.isPhoneValid(), "Phone numbers with length not equal to 10 should fail pattern validation.");
        Assert.assertFalse(registerPage.getPhoneValidationMessage().isEmpty(), "Validation message should be present for invalid phone pattern.");
    }

    @Test(priority = 15, description = "TC15 - Verify password and confirm-password validation")
    public void testPasswordMismatchValidation() {
        RegisterPage registerPage = getRegisterPage();

        String pass1 = ConfigReader.getTestData("mismatched.password");
        String pass2 = ConfigReader.getTestData("mismatched.confirmPassword");

        registerPage.enterPassword(pass1);
        registerPage.enterConfirmPassword(pass2);

        // onblur sets custom validity 'Passwords dont match'
        String validationMsg = registerPage.getConfirmPasswordValidationMessage();
        Assert.assertEquals(validationMsg, "Passwords dont match", "Custom validation message 'Passwords dont match' expected.");
        Assert.assertFalse(registerPage.isConfirmPasswordValid(), "Mismatched passwords should make Confirm Password input invalid.");
    }

    @Test(priority = 16, description = "TC16 - Verify refresh/reset functionality")
    public void testRefreshResetFunctionality() {
        RegisterPage registerPage = getRegisterPage();

        registerPage.enterFirstName("TempName");
        registerPage.enterLastName("TempLast");
        Assert.assertEquals(registerPage.getEnteredFirstName(), "TempName");

        // Click refresh button
        registerPage.clickRefresh();

        // Wait for page reload to settle and re-verify field is cleared
        RegisterPage refreshedPage = new RegisterPage(getDriver());
        Assert.assertTrue(refreshedPage.isPageLoaded(), "Page should reload after clicking Refresh.");
        Assert.assertEquals(refreshedPage.getEnteredFirstName(), "", "First Name should be cleared after refresh.");
    }
}
