package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

/**
 * CheckoutTest — FIX:
 * 1. Dùng Page Objects thay vì driver.findElement() trực tiếp
 * 2. Không hardcode credential
 */
@Feature("Thanh toán")
public class CheckoutTest extends BaseTest {

    private String getUsername() {
        String v = System.getenv("APP_USERNAME");
        return (v != null && !v.isBlank()) ? v : "standard_user";
    }
    private String getPassword() {
        String v = System.getenv("APP_PASSWORD");
        return (v != null && !v.isBlank()) ? v : "secret_sauce";
    }

    @Test(groups = "smoke")
    @Story("UC-010: Luồng checkout hoàn chỉnh")
    @Description("Điền thông tin giao hàng và chuyển sang trang xác nhận")
    @Severity(SeverityLevel.CRITICAL)
    public void checkoutFlow() {
        // FIX: Dùng Page Objects — Fluent Interface
        CheckoutPage checkout = new LoginPage(getDriver())
            .login(getUsername(), getPassword())
            .addFirstItemToCart()
            .goToCart()
            .goToCheckout();

        Allure.step("Điền thông tin giao hàng", () ->
            checkout.fillInfo("Nguyen", "Van A", "700000"));

        Allure.step("Kiểm tra chuyển sang trang overview", () ->
            Assert.assertTrue(checkout.isOnOverviewPage(),
                "Phải chuyển sang trang checkout-step-two"));
    }
}
