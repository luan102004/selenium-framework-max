package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "user-name")   private WebElement usernameField;
    @FindBy(id = "password")    private WebElement passwordField;
    @FindBy(id = "login-button") private WebElement loginButton;
    @FindBy(css = "[data-test='error']") private WebElement errorMsg;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public InventoryPage login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear(); usernameField.sendKeys(username);
        passwordField.clear(); passwordField.sendKeys(password);
        loginButton.click();
        return new InventoryPage(driver);
    }

    public LoginPage loginExpectFail(String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear(); usernameField.sendKeys(username);
        passwordField.clear(); passwordField.sendKeys(password);
        loginButton.click();
        return this;
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOf(errorMsg)).getText().trim();
    }

    public boolean isErrorDisplayed() {
        try { return errorMsg.isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }
}
