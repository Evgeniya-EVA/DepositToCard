import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    @Getter
    private String verificationCode = "12345";
    private SelenideElement code = $("[data-test-id=\"code\"]").$(".input__control");
    private SelenideElement codeVerify = $("[data-test-id=\"action-verify\"]");

    public VerificationPage(){
        code.waitUntil(Condition.visible,15000);
    }

    public DashboardPage codeVerification(String code){
        this.code.setValue(code);
        codeVerify.click();
        return new DashboardPage();
    }
}
