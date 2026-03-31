package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "first-name")  private WebElement firstNameField;
    @FindBy(id = "last-name")   private WebElement lastNameField;
    @FindBy(id = "postal-code") private WebElement postalField;
    @FindBy(id = "continue")    private WebElement continueBtn;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void fillInfo(String first, String last, String postal) {
        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        firstNameField.sendKeys(first);
        lastNameField.sendKeys(last);
        postalField.sendKeys(postal);
        continueBtn.click();
    }

    public boolean isOnOverviewPage() {
        return driver.getCurrentUrl().contains("checkout-step-two");
    }
}
