package truco_java;

import java.util.ArrayList;

public class Person extends Player {
    private int posCards[] = new int[3];

    public Person(ArrayList<Card> cards, boolean firstHand) {
        super(cards, firstHand);
    }

    public int[] getPosCards(){
        return posCards;
    }

    public void setPosCards(int[] p){
        posCards=p;
    }

    public Card throwCard(int pos){
        Card toThrow = cards.get(pos);
        cards.remove(pos);
        playedCards.add(toThrow);
        return toThrow;
    }
}
