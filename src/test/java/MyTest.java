
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;

public class MyTest {

    private BlackjackDealer myDealer(){
        return new BlackjackDealer();
    }
    @Test
    void testCardConstructor(){
        Card myCard = new Card("ace_of_diamonds", 1);
        assertEquals("ace_of_diamonds",myCard.suit,"Incorrect behavior for Card constructor");
        assertEquals(1,myCard.value,"Incorrect behavior for Card constructor");
    }
    @Test
    void testGenerateDeck(){
        BlackjackDealer dealer =  myDealer();
        assertEquals(0,myDealer().deckSize(),"Incorrect behavior for new deck size, expected 0; actual "+ myDealer().deckSize());
        dealer.generateDeck();
        dealer.shuffleDeck();
        assertEquals(52,dealer.deckSize(),"Incorrect behavior for generate deck, expected 52; actual "+ dealer.deckSize());
    }
    @Test
    void testDrawOne(){
        BlackjackDealer dealer = myDealer();
        dealer.generateDeck();
        Card testArray = dealer.drawOne();
        assertNotNull(testArray, "Incorrect drawOne() behavior");
    }
    @Test
    void testDealHand(){
        BlackjackDealer dealer = myDealer();
        dealer.generateDeck();

        ArrayList<Card> testArray = dealer.dealHand();
        int size = testArray.size();
        assertNotNull(testArray, "Incorrect dealHand() behavior");
        assertEquals(2,size, "Incorrect dealHand() behavior. Expected 2 actual " + size);
    }
    @Test
    void testBankerDraw(){
        ArrayList<Card> testArray = new ArrayList<>();
        testArray.add(new Card("4_of_hearts",4));
        testArray.add(new Card("10_of_spades",10));

        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        assertEquals(14, gameLogic.handTotal(testArray));
        assertTrue(gameLogic.evaluateBankerDraw(testArray));
    }
    @Test
    void testHandTotalEdgeCase1(){
        ArrayList<Card> testArray = new ArrayList<>();
        testArray.add(new Card("jack_of_hearts",10));
        testArray.add(new Card("ace_of_spades",1));

        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        assertEquals(21, gameLogic.handTotal(testArray));
    }
    @Test
    void testHandTotalEdgeCase2(){
        ArrayList<Card> testArray = new ArrayList<>();
        testArray.add(new Card("jack_of_hearts",10));
        testArray.add(new Card("ace_of_spades",1));
        testArray.add(new Card("queen_of_clubs",10));

        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        assertEquals(21, gameLogic.handTotal(testArray));
    }
    @Test
    void testHandTotalEdgeCase3(){
        ArrayList<Card> testArray = new ArrayList<>();
        testArray.add(new Card("ace_of_spades",1));
        testArray.add(new Card("ace_of_diamonds",1));
        testArray.add(new Card("ace_of_hearts",1));

        BlackjackGameLogic gameLogic = new BlackjackGameLogic();
        assertEquals(13, gameLogic.handTotal(testArray));
    }
    @Test
    void testWhoWon(){
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        player.add(new Card("ace_of_spades",1));
        player.add(new Card("ace_of_diamonds",1));
        player.add(new Card("ace_of_hearts",1));
        assertEquals(13, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        dealer.add(new Card("jack_of_hearts",10));
        dealer.add(new Card("ace_of_spades",1));
        dealer.add(new Card("queen_of_clubs",10));
        assertEquals(21, gameLogic.handTotal(dealer));

        assertEquals("dealer",gameLogic.whoWon(player,dealer));
    }

    @Test
    void testWinnerWinsByBlackjack1(){
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("ace_of_clubs",1));
        player.add(new Card("jack_of_diamonds",10));

        assertEquals(21, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("9_of_clubs",9));
        dealer.add(new Card("6_of_spades",6));
        dealer.add(new Card("4_of_hearts",4));

        assertEquals(19, gameLogic.handTotal(dealer));
        assertEquals("player", gameLogic.whoWon(player,dealer), "Incorrect gameLogic behavior: " +
                "player should win by blackjack");
    }
    @Test
    void testWinnerWinsByBlackjack2(){
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("ace_of_clubs",1));
        player.add(new Card("jack_of_diamonds",10));

        assertEquals(21, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("9_of_clubs",9));
        dealer.add(new Card("6_of_spades",6));
        dealer.add(new Card("4_of_hearts",4));
        dealer.add(new Card("2_of_hearts",2));

        assertEquals(21, gameLogic.handTotal(dealer));
        assertEquals("player", gameLogic.whoWon(player,dealer), "Incorrect gameLogic behavior: " +
                "player should win by blackjack");
    }

    @Test
    void testWinnerWinsByBlackjack3(){
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("ace_of_clubs",1));
        player.add(new Card("jack_of_diamonds",10));

        assertEquals(21, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("10_of_clubs",10));
        dealer.add(new Card("6_of_spades",6));
        dealer.add(new Card("4_of_hearts",4));
        dealer.add(new Card("ace_of_hearts",1));

        assertEquals(21, gameLogic.handTotal(dealer));
        assertEquals("player", gameLogic.whoWon(player,dealer), "Incorrect gameLogic behavior: " +
                "player should win by blackjack");
    }
    @Test
    void testWinnerWinsByBlackjack4(){
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("ace_of_clubs",1));
        player.add(new Card("jack_of_diamonds",10));

        assertEquals(21, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("10_of_clubs",10));
        dealer.add(new Card("ace_of_hearts",1));

        assertEquals(21, gameLogic.handTotal(dealer));
        assertEquals("push", gameLogic.whoWon(player,dealer), "Incorrect gameLogic behavior: " +
                "game should tie by blackjack");
    }
    @Test
    void testDraw1() {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("ace_of_clubs", 1));
        player.add(new Card("jack_of_diamonds", 10));

        assertEquals(21, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("ace_of_clubs", 1));
        dealer.add(new Card("jack_of_diamonds", 10));

        assertEquals(21, gameLogic.handTotal(dealer));
        assertEquals("push", gameLogic.whoWon(player, dealer), "Incorrect gameLogic behavior: " +
                "game should be tied");
    }
    @Test
    void testDraw2() {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("ace_of_clubs", 1));
        player.add(new Card("jack_of_diamonds", 10));

        assertEquals(21, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("10_of_spades", 10));
        dealer.add(new Card("jack_of_diamonds", 10));
        dealer.add(new Card("ace_of_spades", 1));

        assertEquals(21, gameLogic.handTotal(dealer));
        assertEquals("player", gameLogic.whoWon(player, dealer), "Incorrect gameLogic behavior: " +
                "game should be tied");
    }

    @Test
    void testDraw3() {
        BlackjackGameLogic gameLogic = new BlackjackGameLogic();

        ArrayList<Card> player = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(player));
        player.add(new Card("8_of_clubs", 8));
        player.add(new Card("jack_of_diamonds", 10));

        assertEquals(18, gameLogic.handTotal(player));

        ArrayList<Card> dealer = new ArrayList<>();
        assertEquals(0, gameLogic.handTotal(dealer));

        dealer.add(new Card("10_of_spades", 10));
        dealer.add(new Card("8_of_spades", 8));

        assertEquals(18, gameLogic.handTotal(dealer));
        assertEquals("push", gameLogic.whoWon(player, dealer), "Incorrect gameLogic behavior: " +
                "game should be tied");
    }
}
