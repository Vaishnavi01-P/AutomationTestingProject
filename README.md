# End-to-End Web Automation Testing Framework for Registration Application

[![Java](https://img.shields.io/badge/Java-11%20%7C%2017%20%7C%2021-orange.svg)](https://www.oracle.com/java/)
[![Selenium WebDriver](https://img.shields.io/badge/Selenium%20WebDriver-4.28.1-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-blue.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Build-Maven%203.9+-red.svg)](https://maven.apache.org/)
[![ExtentReports](https://img.shields.io/badge/Reporting-ExtentReports%205-purple.svg)](https://www.extentreports.com/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-Jenkins%20Pipeline-blueviolet.svg)](https://www.jenkins.io/)

A production-grade, enterprise-ready Web UI Automation Framework built from scratch using **Java**, **Selenium WebDriver 4**, **TestNG**, **Maven**, and the **Page Object Model (POM)** architectural pattern.

---

## 📋 Table of Contents
1. [Project Overview & Objectives](#project-overview--objectives)
2. [Application Under Test](#application-under-test)
3. [Technology Stack](#technology-stack)
4. [Framework Architecture & Design Patterns](#framework-architecture--design-patterns)
5. [Directory Structure](#directory-structure)
6. [Test Suite & Scenarios](#test-suite--scenarios)
7. [Prerequisites & Environment Setup](#prerequisites--environment-setup)
8. [Configuration & Test Data Management](#configuration--test-data-management)
9. [Executing Tests](#executing-tests)
10. [Test Reporting & Failure Diagnostics](#test-reporting--failure-diagnostics)
11. [Jenkins CI/CD Pipeline](#jenkins-cicd-pipeline)
12. [Git & GitHub Upload Guide](#git--github-upload-guide)
13. [Troubleshooting Guide](#troubleshooting-guide)

---

## 🎯 Project Overview & Objectives

The primary objective of this project is to automate the end-to-end regression testing of the Demo Automation Registration web application with maximum stability, maintainability, and execution speed.

### Key Highlights:
- **True Page Object Model (POM)**: Complete isolation between page element locators, user interactions, and test assertions.
- **Thread-Safe Driver & Reporting Architecture**: Uses `ThreadLocal<WebDriver>` and `ThreadLocal<ExtentTest>` for seamless parallel execution and clean lifecycle management.
- **Robust Synchronization**: Strictly avoids brittle `Thread.sleep()` pauses; powered by dynamic explicit waits (`WebDriverWait` and `ExpectedConditions`).
- **Dynamic Multi-Control Interactions**: Comprehensive handling of complex UI components including standard dropdowns, Select2 search-and-select widgets, AngularJS multi-select dropdowns, date pickers, radio buttons, checkboxes, file inputs, and HTML5 client-side validation triggers.
- **Automated Failure Capture**: TestNG listener automatically captures timestamped full-screen screenshots upon failure and embeds Base64 screenshots directly into ExtentReports.
- **CI/CD Integration**: Declarative `Jenkinsfile` orchestrating pipeline stages (Checkout, Environment Validation, Build, Test Execution, Report Archiving, and Post-build actions).

---

## 🌐 Application Under Test

- **Target URL**: [https://demo.automationtesting.in/Register.html](https://demo.automationtesting.in/Register.html)
- **Application Type**: Single Page AngularJS Registration Form
- **Target Browsers**: Google Chrome (Primary), Mozilla Firefox, Microsoft Edge, Headless Chrome

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| **Java** | 11 / 17 / 21 / 23 | Core programming language |
| **Selenium WebDriver** | 4.28.1 | Browser automation engine |
| **TestNG** | 7.10.2 | Test runner, lifecycle assertions, and suite management |
| **Maven** | 3.9+ | Build and dependency management tool |
| **WebDriverManager** | 5.9.3 | Automated browser driver binary resolution |
| **ExtentReports** | 5.1.2 | Interactive, rich HTML test execution dashboards |
| **Apache Commons IO** | 2.18.0 | File and directory utilities |
| **Maven Surefire Plugin** | 3.5.2 | CLI test suite execution and JUnit XML report generation |
| **Jenkins** | 2.x | Continuous Integration / Continuous Deployment pipeline |

---

## 🏗️ Framework Architecture & Design Patterns

```
                                    +-----------------------+
                                    |     TestNG XML        |
                                    +-----------+-----------+
                                                |
                                                v
                                    +-----------------------+
                                    |     TestListener      |
                                    +-----+-----------+-----+
                                          |           |
                        +-----------------+           +-----------------+
                        |                                               |
                        v                                               v
            +-----------------------+                       +-----------------------+
            |      BaseTest         |                       | ExtentReportManager   |
            +-----------+-----------+                       +-----------------------+
                        |
            +-----------+-----------+
            |                       |
            v                       v
+-----------------------+ +-----------------------+
|    DriverManager      | |     ConfigReader      |
|  (ThreadLocal Driver) | | (System Override)     |
+-----------+-----------+ +-----------+-----------+
            |                       |
            v                       v
+-----------------------+ +-----------------------+
|     RegisterPage      | |  registration-data    |
| (Locators & Actions)  | |      .properties      |
+-----------+-----------+ +-----------------------+
            |
            v
+-----------------------+
|       WaitUtils       |
|   (Explicit Waits)    |
+-----------------------+
```

### Architectural Principles:
1. **Separation of Concerns**: Test logic (`RegisterTest.java`) only performs business workflow steps and assertions; Page Objects (`RegisterPage.java`) encapsulate web locators and DOM operations.
2. **Centralized Configuration**: All environment URLs, browser targets, timeouts, and report paths are controlled externally via `config.properties`.
3. **Data-Driven Decoupling**: Realistic user test fixtures reside in `registration-data.properties`.
4. **Fault Tolerance**: Automatic fallback to JavaScript execution and presence waits for asynchronous AJAX and AngularJS widgets.

---

## 📂 Directory Structure

```
AutomationTestingProject/
│
├── pom.xml                                         # Maven build configuration and dependencies
├── testng.xml                                      # TestNG suite configuration and listener setup
├── Jenkinsfile                                     # Declarative Jenkins CI/CD pipeline script
├── README.md                                       # Comprehensive project documentation
├── .gitignore                                      # Git ignore rules for build artifacts and temporary files
│
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── pages/
│   │       │   └── RegisterPage.java               # Page Object for Register.html (locators & actions)
│   │       │
│   │       └── utilities/
│   │           ├── ConfigReader.java               # Properties loader with CLI override support
│   │           ├── DriverManager.java              # Thread-safe WebDriver factory and lifecycle manager
│   │           ├── WaitUtils.java                  # Explicit wait synchronization wrapper
│   │           ├── ScreenshotUtils.java            # Failure screenshot capturing utility
│   │           └── ExtentReportManager.java        # ExtentReports 5 spark reporter manager
│   │
│   └── test/
│       ├── java/
│       │   ├── base/
│       │   │   └── BaseTest.java                   # Setup, teardown, browser orchestration
│       │   │
│       │   ├── tests/
│       │   │   └── RegisterTest.java               # 16 Positive and Negative TestNG test cases
│       │   │
│       │   └── listeners/
│       │       └── TestListener.java               # TestNG listener for logging & failure screenshots
│       │
│       └── resources/
│           ├── config.properties                   # Browser, URL, timeout configurations
│           └── testdata/
│               ├── registration-data.properties    # Data-driven test inputs
│               └── sample-photo.png                # Project-relative file for upload automation
│
├── reports/
│   └── ExtentReport.html                           # Generated interactive HTML report
│
├── screenshots/                                    # Target directory for failure screenshots
│
└── target/                                         # Maven compilation outputs and Surefire reports
    └── surefire-reports/
        ├── index.html
        ├── emailable-report.html
        └── TEST-TestSuite.xml
```

---

## 🧪 Test Suite & Scenarios

All 16 test cases implemented in [`RegisterTest.java`](file:///c:/Projects/AutomationTestingProject/src/test/java/tests/RegisterTest.java):

| Test ID | Scenario Description | Scenario Type | Key Validations & Assertions |
|---|---|---|---|
| **TC01** | Verify registration page loads successfully | Positive | URL contains `Register.html`, page title is `Register`, registration form is visible. |
| **TC02** | Verify important registration fields are displayed | Positive | First Name, Last Name, Email, Phone, Gender radio, Submit button presence. |
| **TC03** | Verify valid personal information can be entered | Positive | First Name, Last Name, Address, Email, Phone inputs populated and verified. |
| **TC04** | Verify gender selection | Positive | Radio button selection for Male/FeMale with mutual exclusivity check. |
| **TC05** | Verify hobbies selection | Positive | Cricket, Movies checkboxes selected; unchecked state verified. |
| **TC06** | Verify language selection | Positive | AngularJS multi-select dropdown interaction and item tag presence assertion. |
| **TC07** | Verify skills and country selection | Positive | Standard Skills select dropdown and Select2 searchable Country dropdown. |
| **TC08** | Verify date of birth selection | Positive | Year, Month, and Day select dropdown interaction and value validation. |
| **TC09** | Verify password and confirm password | Positive | Matching password values entered and input value equality verified. |
| **TC10** | Verify file upload | Positive | Relative project file `sample-photo.png` uploaded via sendKeys; filename asserted. |
| **TC11** | Verify complete valid registration workflow | Positive (E2E) | Complete form populated with unique timestamped data, submitted, validity asserted. |
| **TC12** | Verify mandatory field validation | Negative | Blank submission triggers HTML5 browser validation (`checkValidity() == false`). |
| **TC13** | Verify invalid email behavior | Negative | Malformed email string triggers HTML5 `typeMismatch` validation error message. |
| **TC14** | Verify invalid phone behavior | Negative | Non-10-digit phone value triggers pattern regex constraint violation. |
| **TC15** | Verify password mismatch validation | Negative | Mismatching passwords trigger onblur `setCustomValidity('Passwords dont match')`. |
| **TC16** | Verify refresh/reset functionality | Positive | Input fields populated, Refresh clicked, form reload and reset verified. |

---

## ⚙️ Prerequisites & Environment Setup

1. **Java Development Kit (JDK)**: JDK 11 or higher (OpenJDK / Oracle JDK).
   ```bash
   java -version
   ```
2. **Apache Maven**: Version 3.8 or higher.
   ```bash
   mvn -version
   ```
3. **Google Chrome**: Latest stable version installed.

---

## 🔧 Configuration & Test Data Management

### `src/test/resources/config.properties`
```properties
# Browser: chrome, firefox, edge
browser=chrome
headless=false

# Application URL
url=https://demo.automationtesting.in/Register.html

# Timeouts (seconds)
timeout=10
pageLoadTimeout=30
implicitWait=0

# Paths
screenshotPath=screenshots/
extentReportPath=reports/ExtentReport.html
```

### Dynamic CLI Overrides:
Any property can be dynamically overridden from the command line:
```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

---

## 🚀 Executing Tests

### 1. Execute full regression suite via Maven:
```bash
mvn clean test
```

### 2. Execute suite using custom TestNG XML:
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### 3. Execute in Headless Mode (ideal for CI / Docker / Jenkins):
```bash
mvn clean test -Dheadless=true
```

### 4. Cross-browser execution:
```bash
# Microsoft Edge
mvn clean test -Dbrowser=edge

# Mozilla Firefox
mvn clean test -Dbrowser=firefox
```

---

## 📊 Test Reporting & Failure Diagnostics

### 1. ExtentReports HTML Report
Located at `reports/ExtentReport.html`. Provides an interactive dashboard featuring:
- Visual pass/fail metrics and execution timelines.
- Test step logs, categories, and exceptions.
- Embedded Base64 screenshots attached directly to failed test steps.

### 2. TestNG & Surefire Reports
- HTML Suite Report: `target/surefire-reports/index.html`
- Emailable Summary: `target/surefire-reports/emailable-report.html`
- JUnit XML Results: `target/surefire-reports/TEST-TestSuite.xml`

### 3. Failure Screenshots
When a test fails, `TestListener` captures the screenshot into `screenshots/`:
`screenshots/<testMethodName>_<yyyyMMdd_HHmmss_SSS>.png`

---

## 🔄 Jenkins CI/CD Pipeline

The project includes a ready-to-run declarative `Jenkinsfile`.

### Pipeline Stages:
1. **Checkout**: Clones the Git repository.
2. **Environment Check**: Validates `mvn -version` and `java -version`.
3. **Build Project**: Executes `mvn clean compile test-compile`.
4. **Execute Automation Tests**: Runs `mvn test -Dheadless=true`.
5. **Post-Build Actions**:
   - Publishes JUnit XML test results (`target/surefire-reports/*.xml`).
   - Archives ExtentReports (`reports/ExtentReport.html`).
   - Archives failure screenshots (`screenshots/*.png`).
   - Cleans up workspace safely.

### Setting up Jenkins Job:
1. Open Jenkins Dashboard -> **New Item**.
2. Select **Pipeline** and give it a name (e.g. `AutomationTestingProject-Pipeline`).
3. Under **Pipeline Definition**, select **Pipeline script from SCM**.
4. Set SCM to **Git** and enter your repository URL.
5. Set Script Path to `Jenkinsfile`.
6. Configure Global Tool Configuration in Jenkins for JDK (`JDK-17`) and Maven (`Maven-3.9`).
7. Click **Build Now**.

---

## 📤 Git & GitHub Upload Guide

To initialize and push this project to GitHub:

```bash
# 1. Initialize Git in the project root
git init

# 2. Add all files to staging (respects .gitignore)
git add .

# 3. Create initial commit
git commit -m "Initial commit: Complete End-to-End Selenium TestNG POM Framework"

# 4. Link remote repository
git remote add origin https://github.com/<your-username>/AutomationTestingProject.git

# 5. Push to GitHub
git branch -M main
git push -u origin main
```

---

## ❓ Troubleshooting Guide

| Issue | Root Cause | Solution |
|---|---|---|
| `SessionNotCreatedException` | Browser and driver version mismatch | Framework uses `WebDriverManager` which resolves versions automatically. Ensure Chrome is up to date. |
| `ElementClickInterceptedException` | Sticky header / overlay overlapping element | `RegisterPage` uses `scrollIntoView()` and JavaScript click fallbacks for robust element clicks. |
| CI Test execution fails without display | Running on a headless server without X11 | Run with `-Dheadless=true` parameter. |
| `FileNotFoundException` during file upload | Hardcoded local absolute path | Use relative project resource path: `new File("src/test/resources/testdata/sample-photo.png").getAbsolutePath()`. |
