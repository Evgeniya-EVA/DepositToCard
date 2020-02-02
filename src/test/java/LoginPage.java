import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement login = $("[data-test-id=\"login\"").$(".input__control");
    private SelenideElement password = $("[data-test-id=\"password\"]").$(".input__control");
    private SelenideElement button = $("[data-test-id=\"action-login\"");

    public LoginPage(){
        login.waitUntil(Condition.visible, 15000);
    }

    public VerificationPage login(){
        setDefaultAuthData();
        this.button.click();
        return new VerificationPage();
    }

    public void setDefaultAuthData(){
        this.login.setValue("vasya");
        this.password.setValue("qwerty123");
    }

}
