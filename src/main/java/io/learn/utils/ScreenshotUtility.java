package io.learn.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;



public class ScreenshotUtility {
    private ScreenshotUtility(){

    }

    public static String captureScreenshot(WebDriver driver , String screenshotName)
    {
        File srcFile =((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filePath = getFilepath(screenshotName);
        File destFile = new File(filePath);
        try{
            FileUtils.copyFile(srcFile,destFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return filePath;
    }

    private static String getFilepath(String name)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "target/screenshots/" + name + "_" +timeStamp+ ".png";
    }
}
