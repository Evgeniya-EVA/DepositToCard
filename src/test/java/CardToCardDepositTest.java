import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;

public class CardToCardDepositTest {
    private String cardOne = "5559 0000 0000 0001";
    private String cardTwo = "5559 0000 0000 0002";
    private CardInfo customCardTo, customCardFrom;

    @BeforeEach
    void setup(){
        open("http:localhost:9999");
    }

    @AfterEach
    void tearDown(){
        close();
    }

    private void setCardsNewBalance(DashboardPage dashboardPage){
        customCardTo.setNewBalance(dashboardPage.getBalance(customCardTo.getCardShortNumber()));
        customCardFrom.setNewBalance(dashboardPage.getBalance(customCardFrom.getCardShortNumber()));
    }

    private boolean checkNewBalanceChanged(String amount){
        return (customCardTo.isEqualsToAmount(amount) && customCardFrom.isEqualsToAmount(amount));
    }

    private boolean checkBalanceNotChanged(){
        return ((customCardFrom.getBalanceDifference() == 0)
                && (customCardTo.getBalanceDifference() == 0));
    }

    private DashboardPage cardDeposit(CardDepositPage cardDepositPage, String cardNumber, String amount){
        DashboardPage dashboardPage = cardDepositPage.moneyTransfer(amount, cardNumber);
        return dashboardPage;
    }

    private DashboardPage loginDashboardPage(){
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.login();
        String code = verificationPage.getVerificationCode();
        return verificationPage.codeVerification(code);
    }

    private CardDepositPage getBalanceAndGoToCardDepositPage(){
        DashboardPage dashboardPage = loginDashboardPage();
        customCardTo.setStartBalance(dashboardPage.getBalance(customCardTo.getCardShortNumber()));
        customCardFrom.setStartBalance(dashboardPage.getBalance(customCardFrom.getCardShortNumber()));
        return dashboardPage.cardDeposit(customCardTo.getCardShortNumber());
    }

    @Test
    public void validMoneyDepositToCardOne(){
        String amount = "4000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkNewBalanceChanged(amount));
    }

    @Test
    public void validMoneyDepositToCardTwo(){
        String amount = "10000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkNewBalanceChanged(amount));
    }

    @Test
    public void cantDepositToCardOneWithoutNumberOfCardFrom(){
        String amount = "2000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        cardDepositPage.moneyTransfer(amount, ""); // ввод невалидного номера (4 цифры)
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void cantDepositToCardTwoWithoutNumberOfCardFrom(){
        String amount = "2000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        cardDepositPage.moneyTransfer(amount, ""); // ввод невалидного номера (4 цифры)
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void invalidMoneyDepositToCardOneFromInvalidCardNumber(){
        String amount = "2000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        cardDepositPage.moneyTransfer(amount, customCardFrom.getCardShortNumber()); // ввод невалидного номера (4 цифры)
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void invalidMoneyDepositToCardTwoFromInvalidCardNumber(){
        String amount = "2000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        cardDepositPage.moneyTransfer(amount, customCardFrom.getCardShortNumber()); // ввод невалидного номера (4 цифры)
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void cantDepositAmountMoreThenBalance(){
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        String amount = String.valueOf(Integer.parseInt(customCardFrom.getStartBalance()) + 3000);
        cardDepositPage.moneyTransfer(amount, customCardFrom.getCardFullNumber());
        cardDepositPage.waitUntilErrorNotificationAppears();
    }

    @Test
    public void BalanceNotChangedAfterTryingDepositAmountMoreThenBalance(){
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        String amount = String.valueOf(Integer.parseInt(customCardFrom.getStartBalance()) + 3000);
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cantDepositNegativeAmountToCardOneFromCardTwo(){
        String amount = "-2000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue((Integer.parseInt(customCardFrom.getNewBalance())
                - Integer.parseInt(customCardFrom.getStartBalance())) < 0);
    }

    @Test
    public void cantToDepositNegativeAmountToCardTwoFromCardOne(){
        String amount = "-2000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue((Integer.parseInt(customCardFrom.getNewBalance())
                - Integer.parseInt(customCardFrom.getStartBalance())) < 0);
    }

    @Test
    public void invalidMoneyDepositToCardOneFromCardOne(){
        String amount = "2000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void invalidMoneyDepositToCardTwoFromCardTwo(){
        String amount = "2000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDeposit(cardDepositPage, customCardFrom.getCardFullNumber(), amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void checkToCardFieldWhenDepositToCardOne(){
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        String cardNumber = cardDepositPage.getToField().getValue().replaceAll("\\D","");
        Assertions.assertEquals(customCardTo.getCardShortNumber(), cardNumber);
    }

    @Test
    public void checkToCardFieldWhenDepositToCardTwo(){
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        String cardNumber = cardDepositPage.getToField().getValue().replaceAll("\\D","");
        Assertions.assertEquals(customCardTo.getCardShortNumber(), cardNumber);
    }

    @Test
    public void cardsBalanceNotChangedAfterReopenBrowserWhenItWasClosedWhileDepositOperation(){
        String amount = "5000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        cardDepositPage.getAmountField().setValue(amount);
        cardDepositPage.getFromField().setValue(customCardFrom.getCardFullNumber());
        Selenide.close();
        open("http://localhost:9999");
        DashboardPage dashboardPage = loginDashboardPage();
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithEmptyFields(){
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferEmptyFields();
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithFilledAmountField(){
        String amount = "1000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferFilledAmountField(amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithFilledFromField(){
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferFilledFromField(customCardFrom.getCardFullNumber());
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardOneCancelWithAllFieldsFilled(){
        String amount = "1000";
        customCardTo = new CardInfo(cardOne);
        customCardFrom = new CardInfo(cardTwo);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferAllFieldsFilled(amount, customCardFrom.getCardFullNumber());
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithEmptyFields(){
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferEmptyFields();
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithFilledAmountField(){
        String amount = "1000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferFilledAmountField(amount);
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithFilledFromField(){
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferFilledFromField(customCardFrom.getCardFullNumber());
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }

    @Test
    public void cardsBalanceNotChangedAfterDepositToCardTwoCancelWithAllFieldsFilled(){
        String amount = "1000";
        customCardTo = new CardInfo(cardTwo);
        customCardFrom = new CardInfo(cardOne);
        CardDepositPage cardDepositPage = getBalanceAndGoToCardDepositPage();
        DashboardPage dashboardPage = cardDepositPage.cancelTransferAllFieldsFilled(amount, customCardFrom.getCardFullNumber());
        setCardsNewBalance(dashboardPage);
        Assertions.assertTrue(checkBalanceNotChanged());
    }
}
