import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * implements game logic
 * @method + String whoWon(ArrayList <Card> playerHand1, ArrayList <Card> dealerHand)
 * @method + int handTotal(ArrayList <Card> hand)
 * @method + boolean evaluateBankerDraw(ArrayList <Card> banker)
 */
public class BlackjackGameLogic {
    /**
     *
     * @param playerHand1 - ArrayList of player1's cards
     * @param dealerHand - ArrayList of dealer's cards
     * @return player with sum closest to sum to 21 or draw if both players have equal sum
     */
    String whoWon(ArrayList<Card> playerHand1, ArrayList <Card> dealerHand){
        int player1_total = this.handTotal(playerHand1);
        int dealer_total = this.handTotal(dealerHand);

        // if player and dealer have equal scores, game is tied, return push
        if(player1_total == dealer_total){
            if(blackjackWin(playerHand1) && blackjackWin(dealerHand)){
                return "push";
            }
            else if(blackjackWin(playerHand1)){
                return "player";
            }
            else {
                return "dealer";
            }
        }

        // if player has total less than or equal to 21 and greater than dealer, player wins
        if((player1_total <= 21) && (dealer_total >= 22 || (player1_total > dealer_total))) {
            return "player";
        }

        return "dealer"; // Dealer wins if player busts or dealer's total is higher
    }
    protected boolean blackjackWin(ArrayList<Card>hand){
        int sum = 0; boolean hasAnAce = false; boolean hasA10Card = false;
        for(Card card : hand){
            if(card.suit.contains("ace")){ hasAnAce = true; }
            if(card.value == 10){ hasA10Card = true; }
            sum += card.value;
        }
        // if player has an ace card and a card of value 10-> player gets a blackjack win
        return (hasAnAce && hasA10Card) && (sum + 10 == 21);
    }

    /**
     * returns the sum of card values in ArrayList
     * @param hand - ArrayList of player's cards
     * @return sum of Card value in ArrayList
     */
    int handTotal(ArrayList <Card> hand){
        int sum = 0; boolean hasAnAce = false;
        for(Card card : hand){
            if(card.suit.contains("ace")){ hasAnAce = true; }
            sum += card.value;
        }
        return (hasAnAce && (sum + 10 <= 21)) ? sum + 10 : sum;
    }

    /**
     * determines if baker is required to draw at current round
     * @param banker  ArrayList of banker's card
     * @return true if the dealer should draw another card, i.e. if the value is 16 or less.
     */
    boolean evaluateBankerDraw(ArrayList <Card> banker){
        return (this.handTotal(banker) <= 16);
    }
};