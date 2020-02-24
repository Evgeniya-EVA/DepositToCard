import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selenide.*;
import static java.lang.Math.abs;

public class DashboardPage {
    private SelenideElement cardOneInfo = $$(".list__item").first();
    private SelenideElement cardTwoInfo = $$(".list__item").last();
    private SelenideElement cardOneDepositBtn = $$("[data-test-id=\"action-deposit\"]").first();
    private SelenideElement cardTwoDepositBtn = $$("[data-test-id=\"action-deposit\"]").last();;
    private SelenideElement reloadPage = $("[data-test-id=\"action-reload\"]");

    public DashboardPage() {
    }

    public void waitUntilCardInfoIsVisible(){
        cardOneInfo.waitUntil(Condition.visible, 15000);
    }

//    public void setCardsNewBalance(CardInfo cardTo, CardInfo cardFrom){
//        cardTo.setNewBalance(this.getBalance(cardTo.getCardShortNumber()));
//        cardFrom.setNewBalance(this.getBalance(cardFrom.getCardShortNumber()));
//    }

    public String getBalance(String cardNumber){
        String cardInfo;
        if (cardOneInfo.getText().contains(cardNumber)) {
            cardInfo = cardOneInfo.getText();
        } else cardInfo = cardTwoInfo.getText();
        String[] cardInfoSplit = cardInfo.split(", ");
        String[] balanceSplit = cardInfoSplit[1].split(": ");
        return balanceSplit[1].replaceAll("[\\D && [^-]]", "");
    }

    public CardDepositPage selectCardToDeposit(String cardNumber){
        if (cardOneInfo.getText().contains(cardNumber)) {
            cardOneDepositBtn.click();
        } else cardTwoDepositBtn.click();
        return new CardDepositPage();
    }

}
