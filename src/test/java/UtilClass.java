import org.junit.jupiter.api.Assertions;

import static java.lang.Math.abs;

public class UtilClass {
    private static String login = "vasya";
    private static String password = "qwerty123";
    private static String verificationCode = "12345";
    private static String cardFromStartBalance, cardToStartBalance;
    private static String cardFromEndBalance, cardToEndBalance;



    public static DashboardPage loginToDashboardPage(){
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.login(login, password);
        return verificationPage.codeVerification(verificationCode);
    }

    public static void rememberCardsStartBalance(DashboardPage dashboardPage, String cardTo, String cardFrom){
        cardFromStartBalance = dashboardPage.getBalance(cardFrom);
        cardToStartBalance = dashboardPage.getBalance(cardTo);
    }

    public static void rememberCardsEndBalance(DashboardPage dashboardPage, String cardTo, String cardFrom){
        cardFromEndBalance = dashboardPage.getBalance(cardFrom);
        cardToEndBalance = dashboardPage.getBalance(cardTo);
    }

    public static String getAmountMoreThanBalance(){
        int startAmount = Integer.parseInt(cardFromStartBalance);
        return String.valueOf(startAmount + 3000);
    }

    public static int getBalanceDifference(String toOrFrom){
        if (toOrFrom.equals("From")){
            return (Integer.parseInt(cardFromEndBalance) - Integer.parseInt(cardFromStartBalance));
        } else return (Integer.parseInt(cardToEndBalance) - Integer.parseInt(cardToStartBalance));
    }

    public static int getBalanceAbsoluteDifference(String startBalance, String endBalance){
        return abs(Integer.parseInt(endBalance) - Integer.parseInt(startBalance));
    }

    public static void verifyIfCardFromBalanceChangedForAmount(String amount){
        String difference = String.valueOf(getBalanceAbsoluteDifference(cardFromStartBalance, cardFromEndBalance));
        Assertions.assertEquals(amount, difference);
    }

    public static void verifyIfCardToBalanceChangedForAmount(String amount){
        Assertions.assertEquals(amount, String.valueOf(getBalanceAbsoluteDifference(cardToStartBalance, cardToEndBalance)));
    }

    public static void verifyIfCardFromBalanceNotChanged(){
        Assertions.assertTrue(getBalanceAbsoluteDifference(cardFromStartBalance, cardFromEndBalance) == 0);
    }

    public static void verifyIfCardToBalanceNotChanged(){
        Assertions.assertTrue(getBalanceAbsoluteDifference(cardToStartBalance, cardToEndBalance) == 0);
    }

}
