package base;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.testng.*;
import org.testng.annotations.*;
import utils.DriverFactory;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * BaseTest — FIX:
 * 1. Đổi từ `protected WebDriver driver` sang ThreadLocal → an toàn khi chạy song song
 * 2. @AfterMethod nhận ITestResult để chụp screenshot chỉ khi FAIL
 * 3. Đính kèm screenshot vào Allure report (Bài 5)
 * 4. Nhận thêm tham số "env" từ testng.xml
 * 5. Mở URL từ config thay vì hardcode trong mỗi test
 */
public class BaseTest {

    // FIX: ThreadLocal thay vì biến instance → chạy song song không bị race condition
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser,
                      @Optional("dev") String env) {
        System.setProperty("env", env);
        WebDriver driver = DriverFactory.createDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
        // FIX: Mở URL 1 lần ở đây, các test class không cần gọi driver.get() nữa
        driver.get("https://www.saucedemo.com");
        tlDriver.set(driver);
    }

    // FIX: Nhận ITestResult để biết test pass hay fail
    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName());       // lưu file
            attachScreenshotToAllure(getDriver()); // đính vào Allure (Bài 5)
        }
        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove(); // FIX: tránh memory leak khi chạy song song
        }
    }

    public void takeScreenshot(String name) {
        try {
            Files.createDirectories(Path.of("target/screenshots"));
            byte[] bytes = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            Files.write(Path.of("target/screenshots/" + name + ".png"), bytes);
            System.out.println("[Screenshot] target/screenshots/" + name + ".png");
        } catch (Exception e) {
            System.err.println("[Screenshot] Lỗi: " + e.getMessage());
        }
    }

    // Bài 5: Đính kèm ảnh vào Allure report
    @Attachment(value = "Ảnh chụp khi thất bại", type = "image/png")
    public byte[] attachScreenshotToAllure(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
