package io.learn.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.model.ScreenCapture;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.learn.utils.ScreenshotUtility;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.v140.page.model.Screenshot;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;


public class TestListener implements ITestListener {

    private static final ExtentReports extentReports = new ExtentReports();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    static {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-report.html");

        extentReports.attachReporter(sparkReporter);
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extentReports.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
        test.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (extentTest.get() != null) {
            extentTest.get().pass("Test passed");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (extentTest.get() != null) {
            extentTest.get().fail(result.getThrowable());
            WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
            if(driver != null) {
                    String screenshotPath = ScreenshotUtility.captureScreenshot(driver,result.getMethod().getMethodName());
                extentTest.get().addScreenCaptureFromPath(screenshotPath.replace("target", ""));
            }
            else {
                extentTest.get().info("WebDriver instance is null. Unable to capture screenshot.");
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
    }

    public void onTestSkipped(ITestResult result)
    {
        if(extentTest.get() != null)
        {
            extentTest.get().skip("Test skipped: "+result.getMethod().getMethodName());
        }
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }
}
