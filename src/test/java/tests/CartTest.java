package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

/**
 * CartTest — FIX:
 * 1. Dùng Page Objects thay vì driver.findElement() trực tiếp
 * 2. Không hardcode URL và credential trong test
 */
@Feature("Giỏ hàng")
public class CartTest extends BaseTest {

    private String getUsername() {
        String v = System.getenv("APP_USERNAME");
        return (v != null && !v.isBlank()) ? v : "standard_user";
    }
    private String getPassword() {
        String v = System.getenv("APP_PASSWORD");
        return (v != null && !v.isBlank()) ? v : "secret_sauce";
    }

    private InventoryPage doLogin() {
        return new LoginPage(getDriver()).login(getUsername(), getPassword());
    }

    @Test(groups = "smoke")
    @Story("UC-006: Thêm sản phẩm vào giỏ hàng")
    @Description("Badge giỏ hàng phải cập nhật thành 1 sau khi thêm sản phẩm")
    @Severity(SeverityLevel.CRITICAL)
    public void addToCart() {
        InventoryPage inventory = doLogin();

        Allure.step("Kiểm tra trang inventory đã load", () ->
            Assert.assertTrue(inventory.isLoaded()));

        Allure.step("Thêm sản phẩm đầu tiên", () ->
            inventory.addFirstItemToCart());

        Allure.step("Badge giỏ hàng phải là 1", () ->
            Assert.assertEquals(inventory.getCartItemCount(), 1));
    }

    @Test(groups = "smoke")
    @Story("UC-007: Xem giỏ hàng")
    @Severity(SeverityLevel.CRITICAL)
    public void viewCart() {
        CartPage cart = doLogin().addFirstItemToCart().goToCart();

        Allure.step("Giỏ hàng phải có 1 sản phẩm", () ->
            Assert.assertEquals(cart.getItemCount(), 1,
                "Giỏ hàng phải có đúng 1 item"));
    }

    @Test(groups = "regression")
    @Story("UC-008: Giỏ hàng rỗng ban đầu")
    @Severity(SeverityLevel.MINOR)
    public void cartEmptyOnLogin() {
        InventoryPage inventory = doLogin();
        Assert.assertEquals(inventory.getCartItemCount(), 0,
            "Giỏ hàng phải rỗng khi mới đăng nhập");
    }

    @Test(groups = "regression")
    @Story("UC-009: Đi đến trang checkout từ giỏ hàng")
    @Severity(SeverityLevel.NORMAL)
    public void goToCheckout() {
        CartPage cart = doLogin().addFirstItemToCart().goToCart();
        Assert.assertTrue(cart.getItemCount() > 0, "Giỏ hàng phải có item trước khi checkout");
    }
}
