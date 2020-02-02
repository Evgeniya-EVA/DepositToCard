import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.abs;

public class CardInfo {
    @Getter private String cardFullNumber;
    @Getter private String cardShortNumber;
    @Getter @Setter private String startBalance;
    @Getter @Setter private String newBalance;

    public CardInfo(String cardFullNumber){
        this.cardFullNumber = cardFullNumber;
        setCardShortNumber();
    }

    public int getBalanceDifference(){
        return abs(Integer.parseInt(startBalance) - Integer.parseInt(newBalance));
    }

    public boolean isEqualsToAmount(String amount){
        if (getBalanceDifference() == Integer.parseInt(amount)){
            return true;
        } else return false;
    }

    private void setCardShortNumber(){
        this.cardShortNumber = this.cardFullNumber.substring(15);
    }
}
