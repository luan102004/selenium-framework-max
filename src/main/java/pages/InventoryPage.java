package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = ".btn_inventory") private List<WebElement> addButtons;
    @FindBy(css = ".shopping_cart_badge") private WebElement cartBadge;

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        try { return driver.findElement(By.cssSelector(".inventory_list")).isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }

    public InventoryPage addFirstItemToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addButtons.get(0))).click();
        return this;
    }

    public int getCartItemCount() {
        try { return Integer.parseInt(cartBadge.getText().trim()); }
        catch (Exception e) { return 0; }
    }

    public CartPage goToCart() {
        driver.findElement(By.className("shopping_cart_link")).click();
        return new CartPage(driver);
    }
}
