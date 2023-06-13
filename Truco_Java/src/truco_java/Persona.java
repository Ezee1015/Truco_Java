package truco_java;

import java.util.ArrayList;

public class Persona extends Jugador {
    private int posCards[] = new int[3];

    public Persona(ArrayList<Carta> cards, boolean firstHand) {
        super(cards, firstHand);
    }

    public int[] getPosCards(){
        return posCards;
    }

    public void setPosCards(int[] p){
        posCards=p;
    }

    public Carta throwCard(int pos){
        Carta toThrow = cards.get(pos);
        cards.remove(pos);
        playedCards.add(toThrow);
        return toThrow;
    }
}
