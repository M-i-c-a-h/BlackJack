
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;

public class MyTest {
    private BlackjackGame myGameDemo(){
        BlackjackGame game = new BlackjackGame();
        game.gameLogic = new BlackjackGameLogic();
        game.theDealer = new BlackjackDealer();
        game.bankerHand = new ArrayList<>();
        game.playerHand = new ArrayList<>();

        return game;
    }
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
    void testGameWithNoCards(){
        BlackjackGame game = myGameDemo();

        assertEquals(0, game.gameLogic.handTotal(game.playerHand));
        assertEquals(0, game.gameLogic.handTotal(game.bankerHand));
    }
    @Test
    void testWinnerWinsByBlackjack(){
        BlackjackGame game = myGameDemo();

        assertEquals(0, game.gameLogic.handTotal(game.playerHand));
        assertEquals(0, game.gameLogic.handTotal(game.bankerHand));

        game.playerHand.add(new Card("ace_of_clubs",1));
        game.playerHand.add(new Card("jack_of_diamonds",10));

        game.bankerHand.add(new Card("9_of_clubs",9));
        game.bankerHand.add(new Card("6_of_spades",6));
        game.bankerHand.add(new Card("4_of_hearts",4));
        assertEquals("player", game.gameLogic.whoWon(game.playerHand,game.bankerHand), "Incorrect gameLogic behavior: " +
                "player should win by blackjack");
    }
}
