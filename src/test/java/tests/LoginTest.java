package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * LoginTest — FIX:
 * 1. Không còn driver.findElement() trực tiếp — dùng LoginPage (POM)
 * 2. FIX NullPointerException: System.getenv() trả null khi chạy local
 * → dùng helper getCredential() fallback về giá trị mặc định
 * 3. Thêm @Story, @Severity, Allure.step() cho Bài 5
 * 4. Không gọi driver.get() — BaseTest đã mở URL sẵn
 */
@Feature("Đăng nhập hệ thống")
public class LoginTest extends BaseTest {

    /** FIX: Tránh NullPointerException khi APP_USERNAME chưa set (local dev) */
    private String getUsername() {
        return System.getenv("SAUCEDEMO_USERNAME");
    }

    private String getPassword() {
        return System.getenv("SAUCEDEMO_PASSWORD");
    }

    @Test(groups = "smoke")
    @Story("UC-001: Đăng nhập tài khoản hợp lệ")
    @Description("Đăng nhập thành công phải chuyển sang trang inventory")
    @Severity(SeverityLevel.CRITICAL)
    public void loginSuccess() {
        Allure.step("Nhập thông tin đăng nhập hợp lệ", () -> {
        });

        LoginPage loginPage = new LoginPage(getDriver()); // FIX: dùng Page Object
        loginPage.login(getUsername(), getPassword());

        Allure.step("Kiểm tra URL chứa 'inventory'",
                () -> Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"),
                        "Phải chuyển sang trang inventory sau khi đăng nhập thành công"));
    }

    @Test(groups = "smoke")
    @Story("UC-002: Đăng nhập tài khoản bị khoá")
    @Description("Tài khoản locked_out_user phải hiện thông báo lỗi")
    @Severity(SeverityLevel.NORMAL)
    public void loginLockedUser() {
        LoginPage loginPage = new LoginPage(getDriver());

        Allure.step("Đăng nhập với tài khoản bị khoá",
                () -> loginPage.loginExpectFail("locked_out_user", getPassword()));

        Allure.step("Kiểm tra thông báo lỗi hiển thị",
                () -> Assert.assertTrue(loginPage.isErrorDisplayed(), "Phải hiển thị thông báo lỗi"));

        Allure.step("Kiểm tra nội dung lỗi chứa 'locked out'",
                () -> Assert.assertTrue(loginPage.getErrorMessage().contains("locked out")));
    }

    @Test(groups = "regression")
    @Story("UC-003: Đăng nhập sai mật khẩu")
    @Severity(SeverityLevel.NORMAL)
    public void loginFail() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectFail("wrong", "wrong");

        Assert.assertTrue(loginPage.getErrorMessage().contains("Epic sadface"),
                "Thông báo lỗi phải chứa 'Epic sadface'");
    }

    @Test(groups = "regression")
    @Story("UC-004: Username rỗng")
    @Severity(SeverityLevel.MINOR)
    public void loginEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectFail("", getPassword());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
    }

    @Test(groups = "regression")
    @Story("UC-005: Password rỗng")
    @Severity(SeverityLevel.MINOR)
    public void loginEmptyPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectFail(getUsername(), "");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
    }

    // Demo CI fail — uncomment để test log đỏ (Bài 1)
    // @Test(groups = "demo-fail")
    // @Story("DEMO: Test sai cố ý")
    // public void intentionalFail() {
    // Assert.assertEquals("actual", "expected_wrong", "Demo CI fail");
    // }
}
