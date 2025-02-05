package testClasses;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import utils.ReadConfig;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestBaseClass extends ReadConfig {

    public static WebDriver driver;
    public static Logger logger;
    public static ExtentReports extent;
    public static ExtentTest extentTest;

    @BeforeTest
    public void setExtent(){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
        String repName="Test-Report-"+timeStamp+".html";

        extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/"+repName, true);
        extent.addSystemInfo("User Name", "Test User");
        extent.addSystemInfo("Application", "ToDoMVC:React");
        extent.addSystemInfo("Environment", "Dev");

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Drivers\\chromedriver109.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        logger = Logger.getLogger(TestBaseClass.class);
        PropertyConfigurator.configure("log4j.properties");

        driver.get(prop.getProperty("url"));
        logger.info("URL is launched");
    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if(result.getStatus()==ITestResult.FAILURE){
            extentTest.log(LogStatus.FAIL, "Test Case FAILED is "+result.getThrowable());

            String screenshotPath = TestBaseClass.getScreenshot(driver, result.getName());
            extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));
        }
        else if(result.getStatus()==ITestResult.SKIP){
            extentTest.log(LogStatus.SKIP, "Test Case SKIPPED is " + result.getName());
        }
        else if(result.getStatus()==ITestResult.SUCCESS){
            extentTest.log(LogStatus.PASS, "Test Case PASSED is " + result.getName());

        }
        extent.endTest(extentTest);
    }

    public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = "./Screenshots/" + screenshotName + " " + timeStamp + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        logger.info("Screenshot taken");
        return destination;
    }

    @AfterTest
    public void endReport(){
        extent.flush();
        extent.close();
        driver.quit();
    }

}
