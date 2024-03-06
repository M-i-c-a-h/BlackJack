import java.util.ArrayList;

/**
 * Card class creates a card object
 * @method + Card(String theSuit, int theValue)
 * @attribute + string suit - card type (club, spade, diamond, lotus) and face
 * @attribute + int value - numeric val of card (1-10)
 */
public class Card {

    String suit;
    int value;

    /**
     * creates new Card object
     * @param theSuit - card suit
     * @param theValue - card value
     */
    Card(String theSuit, int theValue){
        this.suit = theSuit;
        this.value = theValue;
    }
};

