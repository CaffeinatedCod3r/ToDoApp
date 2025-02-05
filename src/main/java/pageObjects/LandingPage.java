package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ReadConfig;

import java.util.List;

public class LandingPage extends ReadConfig {

    WebDriver driver;

    @FindBy(xpath = "//header[contains(@data-testid,'header')]/h1")
    WebElement mainPageHeading;

    @FindBy(id = "todo-input")
    WebElement inputField;

    @FindBy(xpath = "//footer[contains(@data-testid,'footer')]")
    WebElement taskActions;

    @FindBy(xpath = "//ul[contains(@data-testid,'footer-navigation')]//a[contains(text(),'All')]")
    WebElement allTasks;

    @FindBy(xpath = "//ul[contains(@data-testid,'footer-navigation')]//a[contains(text(),'Active')]")
    WebElement activeTasks;

    @FindBy(xpath = "//ul[contains(@data-testid,'footer-navigation')]//a[contains(text(),'Completed')]")
    WebElement completedTasks;

    @FindBy(xpath = "//span[contains(@class,'todo-count')]")
    WebElement itemsLeftNum;

    @FindBy(xpath = "//label[contains(@class,'toggle-all-label')]")
    WebElement completeAllTasks;

    @FindBy(xpath = "//button[contains(text(),'Clear completed')]")
    WebElement clearCompleted;

    public LandingPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public String getMainPageHeading(){
        return mainPageHeading.getText();
    }

    public void addNewTask(String taskTitle){
        if(taskTitle!=null){
            inputField.sendKeys(taskTitle);
        }
        inputField.sendKeys(Keys.ENTER);
    }

    public void clickActiveTasks(){
        activeTasks.click();
    }

    public void clickCompletedTasks(){
        completedTasks.click();
    }

    public boolean isTaskPresent(String taskName){
        boolean flag = false;
        List<WebElement> tasksList = driver.findElements(By.xpath("//main[contains(@data-testid,'main')]//li"));
        for(WebElement task : tasksList){
            if((task.getText()).equals(taskName)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void completeTask(String taskName){
        WebElement taskCheckbox = driver.findElement(By.xpath(String.format("//label[contains(text(),'%s')]/preceding-sibling::input", taskName)));
        taskCheckbox.click();
    }

    public String editTask(String taskName){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//footer[contains(@data-testid,'footer')]")));
        allTasks.click();
        Actions action = new Actions(driver);
        WebElement task = driver.findElement(By.xpath(String.format("//label[contains(text(),'%s')]", taskName)));
        action.doubleClick(task);
        action.sendKeys("123");
        action.sendKeys(Keys.ENTER);
        return driver.findElement(By.xpath("//label[contains(@data-testid,'todo-item-label')]")).getText();
    }

    public void deleteTask(String taskName){
        allTasks.click();
        WebElement taskToDelete = driver.findElement(By.xpath(String.format("//label[contains(text(),'%s')]/following-sibling::button", taskName)));
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click();",taskToDelete);
    }

    public String countTasks(String taskType){
        if(taskType.equals("All")){
            allTasks.click();
        }else if(taskType.equals("Active")){
            activeTasks.click();
        }else{
            completedTasks.click();
        }
        List<WebElement> numberOfTasks = driver.findElements(By.xpath("//main[contains(@data-testid,'main')]//li"));
        return String.valueOf(numberOfTasks.size());
    }

    public void markAllTasksAsComplete(){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click();",completeAllTasks);
    }

    public void clickClearCompleted(){
        clearCompleted.click();
    }

    public String activeTasksCountNum(){
        String[] words = itemsLeftNum.getText().split(" ");
        return words[0];
    }

}
