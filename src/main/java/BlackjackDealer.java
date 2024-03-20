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

        if(deckSize() > 0){ deck.clear();}

        String[] suit = {"hearts", "diamonds", "spades", "clubs"};
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
        deck.add(new Card(String.format("ace_of_%s",suit),1));
    }

    /**
     * add value cards to deck
     * @param deck - ArrayList of Card objects
     * @param suit - Type of card
     */
    private void addValueCardToDeck(ArrayList<Card> deck, String suit){
        for(int i=2; i<=10; i++){
            deck.add(new Card(String.format("%d_of_%s",i, suit),i));
        }
    }

    /**
     * add face card to deck
     * @param deck - ArrayList of Card objects
     * @param suit - Type of card
     */
    private void addFaceCardToDeck(ArrayList<Card> deck, String suit){
        deck.add(new Card(String.format("queen_of_%s",suit),10));
        deck.add(new Card(String.format("king_of_%s",suit),10));
        deck.add(new Card(String.format("jack_of_%s",suit),10));
    }

    /**
     *  draws two cards from top of deck and returns cards to dealer
     * @return an Arraylist of two cards
     */
    ArrayList<Card> dealHand(){
        ArrayList<Card> hand = new ArrayList<>();
        // draw two cards
        while(hand.size()<2){
            hand.add(drawOne());
        }
        return hand;
    }

    /**
     *
     * @return 1 card from top of deck
     */
    Card drawOne(){
        if(deck.isEmpty()){return null;}

        //draw one
        return deck.remove(0);
    }


    /**
     * shuffles all 52 cards on the deck
     */
    void shuffleDeck(){
        generateDeck();
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
