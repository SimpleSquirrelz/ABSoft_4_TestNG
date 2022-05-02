package net.absoft;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


public class BaseTest {
    @BeforeMethod
    public void sutUp(){
        System.out.println("Zey funkzional ");
    }

    @AfterMethod()
    public void tearDown(){
        System.out.println(" za vuha");
    }
}
