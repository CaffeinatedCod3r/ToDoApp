package testClasses;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.LandingPage;

public class TasksFunctionalityTest extends TestBaseClass {

    // Validate Main Page Heading
    @Test(priority = 1)
    public void validateMainPage(){
        extentTest = extent.startTest("validateMainPage");

        LandingPage lp = new LandingPage(driver);
        String actualHeading = lp.getMainPageHeading();
        Assert.assertEquals(actualHeading,prop.getProperty("mainPageHeading"),"Heading mismatch");
        logger.info("Main Page verified");
    }

    // Validate Adding a new Task
    @Test(priority = 2)
    public void validateNewTaskAdded(){
        extentTest = extent.startTest("validateNewTaskAdded");

        LandingPage lp = new LandingPage(driver);
        lp.addNewTask(prop.getProperty("demoTask"));
        lp.clickActiveTasks();
        Assert.assertTrue(lp.isTaskPresent(prop.getProperty("demoTask")));
        logger.info("New Task is added");
    }

    // Validate count of Active Tasks
    @Test(priority = 3)
    public void validateNumberOfActiveTasks() {
        extentTest = extent.startTest("validateNumberOfActiveTasks");

        LandingPage lp = new LandingPage(driver);
        String totalActiveTasksInList = lp.countTasks("Active");
        String activeTasksNumberInFooter = lp.activeTasksCountNum();
        Assert.assertEquals(totalActiveTasksInList, activeTasksNumberInFooter);
        logger.info("Number of Active tasks verified");
    }

    // Validate Completing an Active Task
    @Test(priority = 4)
    public void validateTaskCompleted(){
        extentTest = extent.startTest("validateCompletedTask");

        LandingPage lp = new LandingPage(driver);
        lp.clickActiveTasks();
        lp.completeTask(prop.getProperty("demoTask"));
        lp.clickCompletedTasks();
        Assert.assertTrue(lp.isTaskPresent(prop.getProperty("demoTask")));
        logger.info("Selected Task is completed");
    }

    // Validate Completing all the Active Tasks
    @Test(priority = 5)
    public void validateCompleteAllTasks(){
        extentTest = extent.startTest("validateCompletedAllTasks");

        LandingPage lp = new LandingPage(driver);
        lp.addNewTask(prop.getProperty("testTask1"));
        lp.addNewTask(prop.getProperty("testTask2"));
        lp.addNewTask(prop.getProperty("testTask3"));
        lp.markAllTasksAsComplete();
        String totalActiveTasks = lp.countTasks("Active");
        Assert.assertEquals(totalActiveTasks, "0");
        logger.info("All Tasks are completed");
    }

    // Validate clearing all the Completed Tasks
    @Test(priority = 6)
    public void validateClearCompleted(){
        extentTest = extent.startTest("validateClearCompleted");

        LandingPage lp = new LandingPage(driver);
        lp.clickClearCompleted();
        lp.clickCompletedTasks();
        String totalCompletedTasks = lp.countTasks("Completed");
        Assert.assertEquals(totalCompletedTasks, "0");
        logger.info("All Completed Tasks are deleted");
    }

    // Validate Editing a Task
    @Test(priority = 7)
    public void validateEditTask(){
        extentTest = extent.startTest("validateEditTask");

        LandingPage lp = new LandingPage(driver);
        String updatedTask = lp.editTask(prop.getProperty("demoTask"));
        logger.info("New updated task name = "+updatedTask);
        Assert.assertNotEquals(prop.getProperty("demoTask"), updatedTask, "Task Name not edited");
        logger.info("Selected Task is updated");
    }

    // Validate Empty task are not added
    @Test(priority = 8)
    public void validateEmptyTaskNotAdded(){
        extentTest = extent.startTest("validateEmptyTaskNotAdded");

        LandingPage lp = new LandingPage(driver);
        String beforeEmptyTask = lp.countTasks("All");
        lp.addNewTask(null);
        String afterEmptyTask = lp.countTasks("All");
        Assert.assertEquals(beforeEmptyTask, afterEmptyTask);
        logger.info("Empty Task not added");
    }

    // Validate Deleting a Task
    @Test(priority = 9)
    public void validateDeleteTask(){
        extentTest = extent.startTest("validateDeleteTask");

        LandingPage lp = new LandingPage(driver);
        lp.deleteTask(prop.getProperty("demoTask"));
        Assert.assertFalse(lp.isTaskPresent(prop.getProperty("demoTask")));
        logger.info("Selected Task is deleted");
    }


}
