package io.learn.tests;

import io.learn.exceptions.LoginFailedException;
import io.learn.pages.CartPage;
import io.learn.pages.LoginPage;
import io.learn.pages.ProductPage;
import io.learn.utils.Browser;
import io.learn.utils.ConfigReader;
import io.learn.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.slf4j.ILoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class baseTest {

    protected WebDriver driver;
    ConfigReader configReader = new ConfigReader();

    private static final Logger logger = LogManager.getLogger(baseTest.class);
    //page object classes
    protected LoginPage loginPage;
    protected ProductPage productPage;
    protected CartPage cartPage;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(ITestContext context , @Optional String browserName)
    {
        logger.debug("Initialize the WebDriver for browser: {}",browserName);
        Browser browser = Browser.valueOf(browserName.toUpperCase());

        logger.debug("Initialize the WebBrowser");
        driver = WebDriverFactory.createDriver(browser);
        context.setAttribute("driver",driver);

        logger.info("Navigating to the base URL: {}", configReader.getProperty("app.url"));
        driver.get(configReader.getProperty("app.url"));

        loginPage = new LoginPage(driver);
        logger.info("Entering credentials and logging in.");
        loginPage.enterCredentials(configReader.getProperty("app.valid.user"),configReader.getProperty("app.password"));

        try {
            productPage = loginPage.clickLogin();
            logger.info("login successful, navigating to product page.");
        }
        catch (LoginFailedException e)
        {
            logger.error("Login failed: {}",e.getMessage());
            throw e; //ensure the test fails if login is not successful
        }

    }

    @AfterClass(alwaysRun = true)
    public void tearDown()
    {
        if(driver != null)
        {
            logger.info("Quitting WebDriver.");//logger statement
            driver.quit();
        }

    }
}
