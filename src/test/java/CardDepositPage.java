import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Value;

import static com.codeborne.selenide.Selenide.$;

@Value
public class CardDepositPage {
    private SelenideElement amountField = $("[data-test-id=\"amount\"").$(".input__control");
    private SelenideElement fromField = $("[data-test-id=\"from\"").$(".input__control");
    private SelenideElement toField = $("[data-test-id=\"to\"").$(".input__control");
    private SelenideElement transferBtn = $("[data-test-id=\"action-transfer\"");
    private SelenideElement cancelBtn = $("[data-test-id=\"action-cancel\"");
    private SelenideElement errorNot = $("[data-test-id=\"error-notification\"");

    public void waitUntilFieldsVisible(){
        amountField.waitUntil(Condition.visible, 15000);
    }

    public DashboardPage moneyTransfer(String amount, String from){
        this.amountField.setValue(amount);
        this.fromField.setValue(from);
        this.transferBtn.click();
        return new DashboardPage();
    }

    public void waitUntilErrorNotificationAppears(){
        errorNot.waitUntil(Condition.visible, 15000);
    }

    public DashboardPage cancelTransferEmptyFields(){
        this.cancelBtn.click();
        return new DashboardPage();
    }

    public DashboardPage cancelTransferFilledAmountField(String amount){
        this.amountField.setValue(amount);
        this.cancelBtn.click();
        return new DashboardPage();
    }

    public DashboardPage cancelTransferFilledFromField(String from){
        this.fromField.setValue(from);
        this.cancelBtn.click();
        return new DashboardPage();
    }

    public DashboardPage cancelTransferAllFieldsFilled(String amount, String from){
        this.amountField.setValue(amount);
        this.fromField.setValue(from);
        this.cancelBtn.click();
        return new DashboardPage();
    }

}
