package truco_java;

import java.util.ArrayList;

public class Player {
  private SetOfCards cards = new SetOfCards();
  private SetOfCards playedCards = new SetOfCards();
  private int points=0;
  private boolean firstHand;

  public Player() {
  }

  public Player(SetOfCards cards, boolean firstHand) {
    this.cards = cards;
    this.firstHand = firstHand;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points, SinglePlayer interfaceGame) {
    this.points = points;
    interfaceGame.updatePoints();
  }

  public void setPoints(int points, ServerMultiplayer interfaceGame) {
    this.points = points;
    interfaceGame.updatePoints();
  }

  public void setPoints(int points, ClientMultiplayer interfaceGame) {
    this.points = points;
    interfaceGame.updatePoints();
  }

  public SetOfCards getCards() {
    return cards;
  }

  public void setCards(SetOfCards cards) {
    this.cards = cards;
  }

  public SetOfCards getPlayedCards() {
    return playedCards;
  }

  public void setPlayedCards(SetOfCards playedCards) {
    this.playedCards = playedCards;
  }

  public boolean isFirstHand() {
    return firstHand;
  }

  public void setFirstHand(boolean firstHand) {
    this.firstHand = firstHand;
  }

  protected int calculateEnvido(){
    int envido=0;
    ArrayList<Card> cardsOriginal = new ArrayList<>();
    cardsOriginal.addAll(cards.toArrayList());
    cardsOriginal.addAll(playedCards.toArrayList());

    // Looks for pairs in order to find an envido of 20
    for(int i=0; i<cardsOriginal.size();i++){
      int c1=i%(cardsOriginal.size()+1);
      int c2=(i+1)%(cardsOriginal.size());

      if(cardsOriginal.get(c1).getStick().equals(cardsOriginal.get(c2).getStick())) {
        int tempEnvido=20;

        if(cardsOriginal.get(c1).getNumber() < 10)
          tempEnvido += cardsOriginal.get(c1).getNumber();
        if(cardsOriginal.get(c2).getNumber() < 10)
          tempEnvido += cardsOriginal.get(c2).getNumber();

        if (tempEnvido>envido) envido=tempEnvido;
      }
    }

    // If there was no more than 20 of envido (there was nos 2 cards of the same
    // stick)
    if (envido==0){
      for(int i=0; i<cardsOriginal.size();i++){
        int num = cardsOriginal.get(i).getNumber();
        if(num < 10 && num > envido) envido= num;
      }
    }

    return envido;
  }

  public void addPlayedCards(int pos){
    playedCards.add(cards.getPosAtVector(pos));
    cards.removeAtVector(pos);
  }

}
