package truco_java;

import java.util.ArrayList;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Player {
  private ArrayList<Card> cards = new ArrayList<>();
  private ArrayList<Card> playedCards = new ArrayList<>();
  private int points=0;
  private boolean firstHand;
  private static final Music effects = new Music();
  private int posCards[] = new int[3];

  public Player() {
  }

  public Player(ArrayList<Card> cards, boolean firstHand) {
    this.cards = cards;
    this.firstHand = firstHand;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points, SinglePlayer interfaceGame) {
    this.points = points;
    try {
      interfaceGame.updatePoints();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Ha sucedido un error en el dibujado del puntaje: " + ex.getMessage());
      effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
      effects.play();
    }
  }

  public void setPoints(int points, ServerMultiplayer interfaceGame) {
    this.points = points;
    try {
      interfaceGame.updatePoints();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Ha sucedido un error en el dibujado del puntaje: " + ex.getMessage());
      effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
      effects.play();
    }
  }

  public void setPoints(int points, ClientMultiplayer interfaceGame) {
    this.points = points;
    try {
      interfaceGame.updatePoints();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Ha sucedido un error en el dibujado del puntaje: " + ex.getMessage());
      effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
      effects.play();
    }
  }

  public ArrayList<Card> getCards() {
    return cards;
  }

  public void setCards(ArrayList<Card> cards) {
    this.cards = cards;
    sortCards();
  }

  public void sortCards(){
    ArrayList<Card> temp = new ArrayList<>();

    for(int x=0;x<cards.size()+temp.size();x++){
      int posMin=0;
      for(int i=1;i<cards.size();i++){
        if(cards.get(i).rankingCard()<cards.get(posMin).rankingCard()){
          posMin=i;
        }
      }
      temp.add(cards.get(posMin));
      cards.remove(posMin);
    }

    cards.addAll(temp);
  }

  public ArrayList<Card> getPlayedCards() {
    return playedCards;
  }

  public void setPlayedCards(ArrayList<Card> playedCards) {
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
    cardsOriginal.addAll(cards);
    cardsOriginal.addAll(playedCards);

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
    playedCards.add(cards.get(pos));
    cards.remove(pos);
  }

  public int[] getPosCards(){
    return posCards;
  }

  public void setPosCards(int[] p){
    posCards=p;
  }

}
