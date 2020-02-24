import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement login = $("[data-test-id=\"login\"]").$(".input__control");
    private SelenideElement password = $("[data-test-id=\"password\"]").$(".input__control");
    private SelenideElement button = $("[data-test-id=\"action-login\"");

    public LoginPage(){
        login.waitUntil(Condition.visible, 15000);
    }

    public VerificationPage login(String login, String password){
        this.login.setValue(login);
        this.password.setValue(password);
        this.button.click();
        return new VerificationPage();
    }

}
