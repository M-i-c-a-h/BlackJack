import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * BlackjackDealer class
 * @method + generateDeck()
 * @method + ArrayList<Card> dealHand()
 * @method + void shuffleDeck()
 * @method + int deckSize()
 */
public class BlackjackDealer {
    ArrayList<Card> deck = new ArrayList<>();

    /**
     * generates playing cards (52 cards, one for each 13 faces 4 units)
     */
    void generateDeck(){
        String[] suit = {"Heart", "Diamond", "Spade", "Club"};
        for(int i=0; i<4; i++){
            addAceCardToDeck(deck, suit[i]);
            addValueCardToDeck(deck, suit[i]);
            addFaceCardToDeck(deck, suit[i]);
        }
    }

    /**
     * add ace card to deck
     * @param deck - ArrayList of Card objects
     * @param suit - Type of card
     */
    private void addAceCardToDeck(ArrayList<Card> deck, String suit){
        deck.add(new Card(String.format("ace of %s",suit),1));
    }

    /**
     * add value cards to deck
     * @param deck - ArrayList of Card objects
     * @param suit - Type of card
     */
    private void addValueCardToDeck(ArrayList<Card> deck, String suit){
        for(int i=2; i<=10; i++){
            deck.add(new Card(String.format("%d of %s",i, suit),i));
        }
    }

    /**
     * add face card to deck
     * @param deck - ArrayList of Card objects
     * @param suit - Type of card
     */
    private void addFaceCardToDeck(ArrayList<Card> deck, String suit){
        deck.add(new Card(String.format("Queen of %s",suit),10));
        deck.add(new Card(String.format("King of %s",suit),10));
        deck.add(new Card(String.format("Jack of %s",suit),10));
    }

    /**
     *  draws two cards from top of deck and returns cards to dealer
     * @return an Arraylist of two cards
     */
    ArrayList<Card> dealHand(){
        ArrayList<Card> hand = new ArrayList<>();
        int count = 0; // allows only 2 draws

        // draw from deck while card drawn is less than 2 and deck is not empty
        while(!deck.isEmpty() && count < 2){
            hand.add(deck.remove(0));
            count++;
        }
        // hand was not completely drawn, add null
        while(hand.size() < 2){
            hand.add(null);
        }
        return hand;
    }


    /**
     * shuffles all 52 cards on the deck
     */
    void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /**
     *
     * @return the number of cards left in the deck
     */
    int deckSize(){
        return deck.size();
    }

};
