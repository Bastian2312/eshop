package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateProductFunctionalTest {
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateProduct() {
        // Navigate to the create product page
        driver.get("http://localhost:8080/product/create");

        // Find and fill the product name input
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.sendKeys("Test Product");

        // Find and fill the product quantity input
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.sendKeys("10");

        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Navigate to the product list page
        driver.get("http://localhost:8080/product/list");

        // Verify that the new product appears in the list
        WebElement table = driver.findElement(By.tagName("table"));
        String pageSource = table.getText();

        assertTrue(pageSource.contains("Test Product"), "Product name not found in list");
        assertTrue(pageSource.contains("10"), "Product quantity not found in list");
    }
}
