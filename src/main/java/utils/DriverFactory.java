package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;

import java.net.URL;
import java.time.Duration;

/**
 * DriverFactory — FIX:
 * 1. Thêm WebDriverManager.setup() để tự động tải đúng chromedriver/geckodriver
 * 2. Thêm --window-size cho headless CI
 * 3. Thêm implicitlyWait cho Grid session
 */
public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        String grid = System.getProperty("grid.url");
        boolean ci = System.getenv("CI") != null;

        // Bài 4: Chạy trên Selenium Grid
        if (grid != null && !grid.isBlank()) {
            try {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setBrowserName(browser.toLowerCase());
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions opts = new ChromeOptions();
                    opts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                    caps.merge(opts);
                }
                RemoteWebDriver d = new RemoteWebDriver(new URL(grid + "/wd/hub"), caps);
                d.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                System.out.println("[DriverFactory] Grid session: " + browser + " | " + d.getSessionId());
                return d;
            } catch (Exception e) {
                throw new RuntimeException("Không kết nối được Grid: " + grid, e);
            }
        }

        // Bài 1: Headless khi chạy CI
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup(); // FIX: thêm setup()
            FirefoxOptions opt = new FirefoxOptions();
            if (ci) opt.addArguments("-headless");
            System.out.println("[DriverFactory] Firefox " + (ci ? "HEADLESS" : "local"));
            return new FirefoxDriver(opt);
        }

        WebDriverManager.chromedriver().setup(); // FIX: thêm setup()
        ChromeOptions opt = new ChromeOptions();
        if (ci) {
            opt.addArguments("--headless=new", "--no-sandbox",
                    "--disable-dev-shm-usage", "--window-size=1920,1080"); // FIX: thêm window-size
        } else {
            opt.addArguments("--start-maximized");
        }
        System.out.println("[DriverFactory] Chrome " + (ci ? "HEADLESS" : "local"));
        return new ChromeDriver(opt);
    }
}
