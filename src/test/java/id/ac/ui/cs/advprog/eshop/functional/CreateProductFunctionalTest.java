package id.ac.ui.cs.advprog.eshop.functional;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateProductFunctionalTest extends BaseFunctionalTest {

    @Test
    public void testCreateProduct(ChromeDriver driver) {
        driver.get(baseUrl + "/product/create");

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        nameInput.sendKeys("Test Product");

        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        quantityInput.sendKeys("10");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        driver.get(baseUrl + "/product/list");

        WebElement table = driver.findElement(By.tagName("table"));
        String pageSource = table.getText();

        assertTrue(pageSource.contains("Test Product"), "Product name not found in list");
        assertTrue(pageSource.contains("10"), "Product quantity not found in list");
    }
}
