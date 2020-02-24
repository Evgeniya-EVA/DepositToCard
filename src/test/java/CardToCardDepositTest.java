import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;

public class CardToCardDepositTest {

    private String cardOneFullName = "5559 0000 0000 0001";
    private String cardTwoFullName = "5559 0000 0000 0002";
    private String cardToShortName, cardFromShortName;
    private String customCardTo, customCardFrom;

    @BeforeEach
    void setup(){
        open("http:localhost:9999");
    }

    @AfterEach
    void tearDown(){
        close();
    }

    @Test
    public void validMoneyDepositToCardOne(){
        String amount = "4000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardFromBalanceChangedForAmount(amount);
        UtilClass.verifyIfCardToBalanceChangedForAmount(amount);
    }

    @Test
    public void validMoneyDepositToCardTwo(){
        String amount = "10000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardFromBalanceChangedForAmount(amount);
        UtilClass.verifyIfCardToBalanceChangedForAmount(amount);
    }

    @Test
    public void cantDepositToCardOneWithoutNumberOfCardFrom(){
        String amount = "2000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        cardDepositPage.moneyTransfer(amount, "");
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void cantDepositToCardTwoWithoutNumberOfCardFrom(){
        String amount = "2000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        cardDepositPage.moneyTransfer(amount, "");
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void invalidMoneyDepositToCardOneFromInvalidCardNumber(){
        String amount = "2000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        cardDepositPage.moneyTransfer(amount, cardFromShortName);
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void invalidMoneyDepositToCardTwoFromInvalidCardNumber(){
        String amount = "2000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        cardDepositPage.moneyTransfer(amount, cardFromShortName);
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void cantDepositAmountMoreThenBalance(){
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        String amount = UtilClass.getAmountMoreThanBalance();
        cardDepositPage.moneyTransfer(amount, customCardFrom);
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void BalanceNotChangedAfterTryingDepositAmountMoreThenBalance(){
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        String amount = UtilClass.getAmountMoreThanBalance();
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardFromBalanceNotChanged();
        UtilClass.verifyIfCardToBalanceNotChanged();
    }

    @Test
    public void cantDepositNegativeAmountToCardOneFromCardTwo(){
        String amount = "-2000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        Assertions.assertTrue(UtilClass.getBalanceDifference("From") <= 0);
    }

    @Test
    public void cantToDepositNegativeAmountToCardTwoFromCardOne(){
        String amount = "-2000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        Assertions.assertTrue(UtilClass.getBalanceDifference("From") < 0);
    }

    @Test
    public void invalidMoneyDepositToCardOneFromCardOne(){
        String amount = "2000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void invalidMoneyDepositToCardTwoFromCardTwo(){
        String amount = "2000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.moneyTransfer(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void checkToCardFieldWhenDepositToCardOne(){
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        String cardNumber = cardDepositPage.getCardToInput().getValue().replaceAll("\\D","");
        Assertions.assertEquals(cardToShortName, cardNumber);
    }

    @Test
    public void checkToCardFieldWhenDepositToCardTwo(){
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        String cardNumber = cardDepositPage.getCardToInput().getValue().replaceAll("\\D","");
        Assertions.assertEquals(cardToShortName, cardNumber);
    }

    @Test
    public void cardsBalanceNotChangedAfterReopenBrowserWhenItWasClosedWhileDepositOperation(){
        String amount = "5000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        cardDepositPage.setAmountInput(amount);
        cardDepositPage.setCardFromInput(customCardFrom);
        Selenide.close();
        open("http://localhost:9999");
        dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithEmptyFields(){
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferEmptyFields();
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithFilledAmountField(){
        String amount = "1000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferFilledAmountField(amount);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithFilledFromField(){
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferFilledFromField(customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithAllFieldsFilled(){
        String amount = "1000";
        customCardTo = cardOneFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardTwoFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferAllFieldsFilled(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithEmptyFields(){
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferEmptyFields();
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithFilledAmountField(){
        String amount = "1000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferFilledAmountField(amount);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithFilledFromField(){
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferFilledFromField(customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithAllFieldsFilled(){
        String amount = "1000";
        customCardTo = cardTwoFullName;
        cardToShortName = customCardTo.substring(15);
        customCardFrom = cardOneFullName;
        cardFromShortName = customCardFrom.substring(15);
        DashboardPage dashboardPage = UtilClass.loginToDashboardPage();
        UtilClass.rememberCardsStartBalance(dashboardPage, cardToShortName, cardFromShortName);
        CardDepositPage cardDepositPage = dashboardPage.selectCardToDeposit(cardToShortName);
        dashboardPage = cardDepositPage.cancelTransferAllFieldsFilled(amount, customCardFrom);
        UtilClass.rememberCardsEndBalance(dashboardPage, cardToShortName, cardFromShortName);
        UtilClass.verifyIfCardToBalanceNotChanged();
        UtilClass.verifyIfCardFromBalanceNotChanged();
    }
}
