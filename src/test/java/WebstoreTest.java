import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebstoreTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Setup Chrome WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--headless=new"); // Maximize browser window
        driver = new ChromeDriver(options);
    }

    @Test
    public void testWebstoreSearch() {
        // Navigate to the webstore website
        String url = "https://www.webstaurantstore.com/";
        driver.get(url);

        // Search for 'stainless work table'
        WebElement searchBox = driver.findElement(By.id("searchval"));
        searchBox.sendKeys("stainless work table");
        searchBox.submit();

        // Wait for search results to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product_listing")));

        // Check search results for 'Table' in title
        for (WebElement product : driver.findElements(By.id("page"))) {
            assertTrue("Product title contains 'Table'", product.getText().toLowerCase().contains("table"));
        }
        driver.findElement(By.cssSelector("#paging > nav > ul > li:nth-child(7) > a")).click();

        // Clicking on the product
        WebElement lastProduct = driver.findElement(By.xpath("//*[@id=\"ProductBoxContainer\"]/div[4]/form/div/div/input[2]"));
        lastProduct.click();

        // Open shopping cart
        WebElement selectViewCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cartItemCountSpan")));
        selectViewCart.click();

        // Remove item from the cart
        WebElement itemDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("itemDelete__icon")));
        itemDelete.click();

        // Verify the empty cart text
        WebElement getEmptyCartText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/div[1]/div[1]/div/div[2]/p[1]")));
        String text = getEmptyCartText.getText();
        assertEquals("Your cart is empty.", text);
    }

    @After
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
