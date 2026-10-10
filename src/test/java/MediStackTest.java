
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

public class MediStackTest {

    private WebDriver driver;
    private WebDriverWait wait;

   /*  @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        // Selenium Manager automatically manages ChromeDriver
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(
            driver, Duration.ofSeconds(15)
        );
    } */

    @BeforeMethod
public void setUp() {
    ChromeOptions options = new ChromeOptions();

    options.addArguments("--headless=new");
    options.addArguments("--window-size=1920,1080");

    // Useful when Jenkins runs on Linux
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");

    // Selenium Manager resolves the ChromeDriver
    driver = new ChromeDriver(options);

    wait = new WebDriverWait(
        driver, Duration.ofSeconds(15)
    );
}

    @Test
    public void testMediStackWebsite() {

        // Open MediStack
        driver.get("https://medistack.vercel.app/");

        // Wait until the page is loaded
        wait.until(d ->
            ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete")
        );

        // Verify page title
        System.out.println("Page title: " + driver.getTitle());
        Assert.assertFalse(
            driver.getTitle().trim().isEmpty(),
            "Page title should not be empty"
        );

        // Verify URL
        Assert.assertTrue(
            driver.getCurrentUrl().contains("medistack.vercel.app"),
            "Incorrect website URL"
        );

        // Verify visible page content
        WebElement body = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.tagName("body")
            )
        );

        String pageText = body.getText();

        Assert.assertFalse(
            pageText.trim().isEmpty(),
            "Page should contain visible text"
        );

        System.out.println("Page content:\n" + pageText);

        // Print visible links
        List<WebElement> links =
            driver.findElements(By.tagName("a"));

        System.out.println("\nVisible links:");

        for (WebElement link : links) {
            if (link.isDisplayed()) {
                String text = link.getText().trim();
                String href = link.getAttribute("href");

                if (!text.isEmpty()) {
                    System.out.println(text + " -> " + href);
                }
            }
        }

        // Capture screenshot
        try {
            org.openqa.selenium.OutputType.FILE
                .getClass(); // No-op
            FileScreenshot.save(driver, "medistack-homepage.png");
        } catch (Exception e) {
            System.out.println(
                "Screenshot error: " + e.getMessage()
            );
        }

        System.out.println("MediStack smoke test passed.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Helper class for screenshot capture
    static class FileScreenshot {
        static void save(WebDriver driver, String filename)
                throws java.io.IOException {
            java.io.File source =
                ((org.openqa.selenium.TakesScreenshot) driver)
                    .getScreenshotAs(
                        org.openqa.selenium.OutputType.FILE
                    );

            java.nio.file.Files.copy(
                source.toPath(),
                java.nio.file.Paths.get(filename),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        }
    }
}
