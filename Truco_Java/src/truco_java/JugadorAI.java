package truco_java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class JugadorAI extends Jugador {
  private int playersDeclaredEnvido = -1;
  private static final Music effects = new Music();

  public JugadorAI(ArrayList<Carta> cards, boolean firstHand) {
    super(cards, firstHand);
  }

  public Carta playTurnAlgorithm(Persona p, InterfazJuego interfaceGame) throws IOException{
    ArrayList<Carta> playerPlayedCards = p.getPlayedCards();

    // If it's the first round and the player didn't play
    if(playerPlayedCards.isEmpty())
      return throwRandomCard();

    // If the player has already played and it's the AI's turn
    if(playerPlayedCards.size() > playedCards.size()) {
      // From the worst to the best card (already sorted)
      for(int i=0;i<cards.size();i++) {
        // If there is a card that ties the one thrown by the player, and I won the first round. Throw it
        if( playerPlayedCards.size()>1 &&
            playerPlayedCards.get(playerPlayedCards.size()-1).rankingCard() == cards.get(i).rankingCard() &&
            p.getPlayedCards().get(0).rankingCard() <= cards.get(0).rankingCard()
          )
          return throwCardAtPos(i);
        // Looks for the worst card that can win
        if(playerPlayedCards.get(playerPlayedCards.size()-1).rankingCard() < cards.get(i).rankingCard())
          return throwCardAtPos(i);
      }
      // If I can't win that card, throw the worst one
      return throwWorstCard();
    }

    if(playedCards.size()>=1){
      if(playerPlayedCards.get(0).rankingCard() == playedCards.get(0).rankingCard())
        return throwBestCard();
      if(playerPlayedCards.get(0).rankingCard() > playedCards.get(0).rankingCard()){
        // From the worst to the best card (already sorted)
        for(int i=0;i<cards.size();i++) {
          // If there is a card that ties the one thrown by the player, and I won/tie the first round. Throw it
          if(
              p.getPlayedCards().size()>=2 &&
              p.getPlayedCards().get(p.getPlayedCards().size()-1).rankingCard() == cards.get(i).rankingCard() &&
              p.getPlayedCards().get(0).rankingCard() <= cards.get(0).rankingCard()
            )
            return throwCardAtPos(i);
          // Looks for the worst card that can win
          if(p.getPlayedCards().get(p.getPlayedCards().size()-1).rankingCard() < cards.get(i).rankingCard())
            return throwCardAtPos(i);
        }
        return throwBestCard();
      }
      if(p.getPlayedCards().get(0).rankingCard() < playedCards.get(0).rankingCard())
        return throwWorstCard();
    }

    return throwBestCard();
  }

  public Carta throwRandomCard(){
    return throwCardAtPos(new Random().nextInt(cards.size()));
  }

  public Carta throwCardAtPos (int pos){
    Carta toThrow = cards.get(pos);
    cards.remove(pos);
    playedCards.add(toThrow);
    return toThrow;
  }

  public Carta throwBestCard (){
    if(cards.isEmpty()) return null;

    int bestCardPos = 0;
    for(int i=1; i<cards.size(); i++){
      if(cards.get(bestCardPos).rankingCard() < cards.get(i).rankingCard())
        bestCardPos = i;
    }

    return throwCardAtPos(bestCardPos);
  }

  public Carta throwWorstCard (){
    if(cards.isEmpty()) return null;

    int worstCardPos = 0;
    for(int i=1; i<cards.size(); i++){
      if(cards.get(worstCardPos).rankingCard() > cards.get(i).rankingCard())
        worstCardPos = i;
    }

    return throwCardAtPos(worstCardPos);
  }

  public int envidoAlgorithm(int envidoLevel, Persona p, Truco_Java menu){
    /*
        States:
        0 --> No quiero / No se cantó
        1 --> Envido
        2 --> Envido - Envido
        3 --> Real Envido
        4 --> Falta Envido
        ** If it returns the same number that was given to the function, that's interpreted as 'quiero'
     */

    int decision=0, obligated=envidoLevel;
    Random random = new Random();

    // Less than 23 of envido, do nothing
    if(calculateEnvido()<=23){
      // The number '7' it's a random hard-coded number for a probability of 1 in 8
      if(random.nextInt(8) == 7 && !menu.easyCheckBox.isSelected())
        decision = envidoLevel + 1;
      return decision;
    }

    // More than 25 of envido and it was nos declared, obligate to declare
    if(envidoLevel==0 && calculateEnvido()>25)
      obligated = 1;

    // More than 30 of envido, declare
    if(calculateEnvido()>30){
      if(envidoLevel==4)
        return 4;
      decision = obligated + random.nextInt(4-obligated);
    }

    else if(calculateEnvido()>27){
      if(envidoLevel==3)
        decision = 3;
      else if(envidoLevel<4)
        decision = obligated + random.nextInt(3-obligated);
      else
        decision = 0;
    }

    else if(calculateEnvido()>25){
      if(envidoLevel<3) {
        if (envidoLevel==2)
          decision = 2;
        else
          decision = obligated + random.nextInt(2 - obligated);
      } else
        decision = 0;
    }

    else if(calculateEnvido()>23){
      switch (envidoLevel) {
        case 1:
          decision = 1;
          break;
        case 0:
          decision = random.nextInt(1-envidoLevel);
          break;
        default:
          decision = 0;
          break;
      }
    }

    // If envido was declare and the player and the player is one point away from winning, accept
    if(envidoLevel>0 && p.getPoints()==14){
      if(envidoLevel<3)
        decision = envidoLevel+1;
      else
        decision = envidoLevel;
    }

    if(decision==2 && envidoLevel!=1){
      if(calculateEnvido()>27)
        return 3;
      else {
        if(envidoLevel<2) return 1;
        else return 0;
      }
    }

    // If it replies in a lower level that the offer, don't want
    if(decision<envidoLevel && decision!=0)
      return 0;

    // Adds an option to risk
    if(
        decision==0 && envidoLevel>=3 && calculateEnvido()>26 &&
        random.nextInt(2)==0 && !menu.easyCheckBox.isSelected()
      )
      return envidoLevel;

    if(
        decision==0 &&
        envidoLevel==0 &&
        firstHand==false &&
        random.nextInt(4) == 3 &&
        !menu.easyCheckBox.isSelected()
      )
      return 1;

    return decision;
  }

  private int goodCardsCount () {
    int cant=0;

    for (int i = 0; i < cards.size(); i++)
      if(cards.get(i).rankingCard()>7)
        cant++;

    return cant;
  }

  private int mediumCardCount () {
    int cant=0;

    for (int i = 0; i < cards.size(); i++)
      if(cards.get(i).rankingCard()>4 && cards.get(i).rankingCard()<8)
        cant++;

    return cant;
  }

  public int trucoAlgorithm (int trucoLevel, Persona p, Truco_Java menu) {
    /*
      States:
      0 --> No quiero / No se cantó
      1 --> Truco
      2 --> Retruco
      3 --> Vale 4
       ** If it returns the same number that was given to the function, that's interpreted as 'quiero'
     */

    if(menu.easyCheckBox.isSelected())
      return easyTrucoAlgorithm(trucoLevel, p);

    Random random = new Random();

    int cardsPlayedCount = p.getPlayedCards().size() + playedCards.size();

    // If truco was declared and the player is one point away to win, accept or reply truco
    if(trucoLevel>0 && p.getPoints()==14){
      if(trucoLevel!=3)
        return trucoLevel+1;
      else
        return trucoLevel;
    }

    switch(cardsPlayedCount){
      case 0:
      case 1: // First round
        if(trucoLevel == 0)
          return 0;
        else {
          if(goodCardsCount()>=1) {
            if(mediumCardCount()>=1){
              if(trucoLevel==3)
                return 3;
              return trucoLevel+1;
            } else if(random.nextInt(2)==1)
              return trucoLevel;
          }
        }
        break;
      case 4:
      case 5: // Third round
        int probabilityToWin = predictCardWithEnvido(p.getPlayedCards());
        switch(probabilityToWin){
          case 0: break;
          case 1:
                  if(trucoLevel==3)
                    return 3;
                  return trucoLevel+1;
          case 2:
                  if(random.nextInt(3)==1){
                    if(trucoLevel==3)
                      return 3;
                    return trucoLevel+1;
                  }
                  break;
          case 3:
                  return 0;
        }
        // If it's in the last round and AI is the last one to throw
        if(p.getPlayedCards().size()-1 == playedCards.size()){
          if(cards.get(0).rankingCard() > p.getPlayedCards().get(2).rankingCard()) // If AI wins, declare
            return trucoLevel+random.nextInt(4-trucoLevel);
          // if it's a tie, but AI won the first
          else if(
              cards.get(0).rankingCard() > p.getPlayedCards().get(2).rankingCard() &&
              p.getPlayedCards().get(0).rankingCard() == cards.get(0).rankingCard()
            )
            return trucoLevel+random.nextInt(4-trucoLevel);
          // If AI looses: It can randomly reply (only if it isn't 'vale 4') and the card
          // that the player has thrown is worse than an 'ancho falso'
          else if(
              random.nextInt(3)==1 && trucoLevel!=3 &&
              p.getPlayedCards().get(2).rankingCard()<8
              )
            return trucoLevel+random.nextInt(3-trucoLevel)+1;
        // If the last card is a good one
        } else if(playedCards.size() == 3 && playedCards.get(2).rankingCard()>9) {
          if(trucoLevel == 3)
            return 3;
          return trucoLevel+random.nextInt(3-trucoLevel);
        } else if(random.nextInt(4)==2)
          return trucoLevel;
        // If its in the last round and the only thing left is the opponent to throw
        if(getPlayedCards().size()-1 == p.getPlayedCards().size()){
          // If it's good, declare
          if(getPlayedCards().get(2).rankingCard() > 6){
            if(trucoLevel == 3)
              return 3;
            // Go all in
            return trucoLevel+random.nextInt(3-trucoLevel);
          }
          // Go all in
          if(getPlayedCards().get(2).rankingCard() > 1 && getPlayedCards().get(2).rankingCard() < 6 && random.nextInt(3)==1){ // Apuesto todo
            if(trucoLevel == 3)
              return 3;
            return trucoLevel+random.nextInt(3-trucoLevel);
          }
        }
        break;
      case 2:
      case 3: // Second round
        // If AI has a good card and a medium one
        if(goodCardsCount()==1 && mediumCardCount()==1){
          if(trucoLevel==3)
            return 3;
          return trucoLevel+random.nextInt(3-trucoLevel);
        }

        // If AI has more than one good card, go all in
        if(goodCardsCount()>1){
          if(trucoLevel==3)
            return 3;
          return trucoLevel+random.nextInt(3-trucoLevel);
        }

        // If AI has a good card, accept
        if(goodCardsCount()==1 && trucoLevel!=0)
          return trucoLevel;

        // If I tie the previous round
        if(p.getPlayedCards().get(p.getPlayedCards().size()-1).rankingCard() == playedCards.get(playedCards.size()-1).rankingCard()){
          if(goodCardsCount()>=1){
            if(trucoLevel==3)
              return 3;
            return trucoLevel+random.nextInt(3-trucoLevel);
          }
          if(mediumCardCount()>=1 && random.nextInt(2)==1)
            return trucoLevel;
        }
        // If AI has already thrown but is the turn of the player, and the cards left to play are no good because AI already played
        if(p.getPlayedCards().size()+1==playedCards.size()){
          if(playedCards.get(playedCards.size()-1).rankingCard()>7) {
            if(trucoLevel==3)
              return 3;
            return trucoLevel+random.nextInt(3-trucoLevel);
          }
          // If the card throwed was not a good card, but else, a medium one
          if(playedCards.get(playedCards.size()-1).rankingCard()>5) {
            if(trucoLevel==3)
              return 3;
            return trucoLevel+random.nextInt(3-trucoLevel);
          }
        }
        // If AI has more than one medium card, and the trucoLevel is less than 'retruco', randomly accept or not
        if(mediumCardCount()>1 && trucoLevel<=2 && random.nextInt(3)==2)
          return trucoLevel;
        break;
    }

    return 0;
  }

  public int easyTrucoAlgorithm (int trucoLevel, Persona p) {
    /*
      States:
      0 --> No quiero / No se cantó
      1 --> Truco
      2 --> Retruco
      3 --> Vale 4
       ** If it returns the same number that was given to the function, that's interpreted as 'quiero'
     */

    Random random = new Random();

    int cardsPlayedCount = p.getPlayedCards().size() + playedCards.size();

    switch(cardsPlayedCount){
      case 0:
      case 1: // First round
        if(trucoLevel == 0)
          return 0;
        else {
          if(goodCardsCount()>=1 && mediumCardCount()>=1)
            return trucoLevel;
        }
        break;
      case 4:
      case 5: // Third round
        // If it's in the last round and AI is the last one to throw
        if(p.getPlayedCards().size()-1 == playedCards.size()){
          if(cards.get(0).rankingCard() > p.getPlayedCards().get(2).rankingCard()) // If AI wins, declare
            return trucoLevel+random.nextInt(4-trucoLevel);
          // if it's a tie, but AI won the first
          else if(cards.get(0).rankingCard() > p.getPlayedCards().get(2).rankingCard() && p.getPlayedCards().get(0).rankingCard() == cards.get(0).rankingCard())
            return trucoLevel+random.nextInt(4-trucoLevel);
        // If the last card is a good one
        } else if(playedCards.size() == 3 && playedCards.get(2).rankingCard()>9) {
          // Go all in
          if(trucoLevel == 3)
            return 3;
          return trucoLevel+random.nextInt(3-trucoLevel);
        }
        break;
      case 2:
      case 3: // Second round
        // If AI has more than one good card, go all in
        if(goodCardsCount()>1){
          if(trucoLevel==3)
            return 3;
          return trucoLevel+random.nextInt(3-trucoLevel);
        }
        // If I tie the previous round
        if(p.getPlayedCards().get(p.getPlayedCards().size()-1).rankingCard() == playedCards.get(playedCards.size()-1).rankingCard()){
          if(goodCardsCount()>=1){
            if(trucoLevel==3)
              return 3;
            return trucoLevel+random.nextInt(3-trucoLevel);
          }
        }
        break;
    }

    return 0;
  }

  public int getPlayersDeclaredEnvido() {
    return playersDeclaredEnvido;
  }

  public void setPlayersDeclaredEnvido(int playersDeclaredEnvido) {
    this.playersDeclaredEnvido = playersDeclaredEnvido;
  }

  // This function is only called when the player and the AI have already thrown 2
  // cards each and the 'envido' was played.
  // Calculates tries to predict the card left of the player with the 'envido' declared
  private int predictCardWithEnvido(ArrayList<Carta> playerPlayedCards) {
    if((playerPlayedCards.size()!=2 && playedCards.size()>=2) || playersDeclaredEnvido==-1)
      return 0;
    /*
      Returns:
      0 --> Unknown
      1 --> AI wins
      2 --> There's a change to win
      3 --> Player wins
    */
    ArrayList<String> thrownSticks = new ArrayList<>(); // Variable to store thrown sticks
    for(int i=0;i<2;i++)
      thrownSticks.add(playerPlayedCards.get(i).getStick());
    ArrayList<Integer> thrownNumbers = new ArrayList<>(); // Variable to store thrown numbers
    for(int i=0;i<2;i++)
      thrownNumbers.add(playerPlayedCards.get(i).getNumber());

    Carta aiLastCard;
    if(playedCards.size()==2)
      aiLastCard=cards.get(0);
    else
      aiLastCard=playedCards.get(2);

    if(playersDeclaredEnvido==0){ // two jacks, horses or kings
      if(aiLastCard.rankingCard()<4)
        return 3;
      if(aiLastCard.rankingCard()>6)
        return 1;
      return 2;
    }


    if(playersDeclaredEnvido==1){
      if(thrownNumbers.contains(1))
        return 0;
      if(thrownSticks.contains("espada") || thrownSticks.contains("basto")){
        if(aiLastCard.rankingCard()==13)
          return 1;
        return 3;
      }

      if(aiLastCard.rankingCard()>7)
        return 1;
      if(aiLastCard.rankingCard()==7)
        return 2;
      return 3;
    }

    if(playersDeclaredEnvido<=3) {
      if(thrownNumbers.contains(2) || thrownNumbers.contains(3))
        return 0;
      if(aiLastCard.rankingCard()>9)
        return 1;
      if(aiLastCard.rankingCard()>7)
        return 2;
      return 3;
    }

    if(playersDeclaredEnvido<=6){
      if(thrownNumbers.contains(playersDeclaredEnvido))
        return 0;

      // If AI wins to the number (the stick 'basto' is random)
      if(aiLastCard.rankingCard() > new Carta(playersDeclaredEnvido, "basto").rankingCard())
        return 1;
    }

    if(playersDeclaredEnvido==7) {
      if(thrownNumbers.contains(7))
        return 0;
      if(thrownSticks.contains("basto") || thrownSticks.contains("copa")){
        if(aiLastCard.rankingCard()>10)
          return 1;
        return 3;
      }
      if(aiLastCard.rankingCard()>3)
        return 1;
      return 3;
    }

    if(playersDeclaredEnvido==20){
      // First one is a jack, horse or king
      if(thrownNumbers.get(0) >= 10 && thrownNumbers.get(0) <= 12)
        // The same with the second round
        if(thrownNumbers.get(1) >= 10 && thrownNumbers.get(1) <= 12)
          // And they're both from the same stick
          if(thrownSticks.get(0).equals(thrownSticks.get(1)))
            return 0;
      if(aiLastCard.rankingCard()<4)
        return 3;
      if(aiLastCard.rankingCard()>6)
        return 1;
      return 2;
    }

    if(playersDeclaredEnvido>20){
      ArrayList<Carta> possibilities = new ArrayList<>();
      // Looks for the probability of cards that the player can have
      for (int i = 0; i < 2; i++) {
        int numberPosibility;

        // If it's from 1 to 7, counts as normal
        if(thrownNumbers.get(i)<10)
          numberPosibility = playersDeclaredEnvido - thrownNumbers.get(i) - 20;
        // If it a 10, 11 or 12, counts as 0 (envido)
        else
          numberPosibility = playersDeclaredEnvido - 20;
        // If they have a jack, horse or king left
        if(numberPosibility==0){
          possibilities.add(new Carta(10, thrownSticks.get(i)));
          possibilities.add(new Carta(11, thrownSticks.get(i)));
          possibilities.add(new Carta(12, thrownSticks.get(i)));
        }
        // If it's from 1 to 7
        if (numberPosibility>0 && numberPosibility<8) // Si el le queda un numero de carta
          possibilities.add(new Carta(numberPosibility, thrownSticks.get(i)));
      }

      // Deletes from the remaining card possibilities, all the cards I have or had
      for (int i = 0; i < possibilities.size(); i++) {
        for (int x = 0; x < cards.size(); x++)
          if(possibilities.get(i).equals(cards.get(x)))
            possibilities.remove(i);
        for (int x = 0; x < playedCards.size(); x++)
          if(possibilities.get(i).equals(playedCards.get(x)))
            possibilities.remove(i);
        for (int x = 0; x < playerPlayedCards.size(); x++)
          if(possibilities.get(i).equals(playerPlayedCards.get(x)))
            possibilities.remove(i);
      }

      // If the two cards that the player had already thrown are the exact number of
      // envido that that person has, that means that they had already played the
      // cards that were in the 'envido', so the remaining card is unknown
      if(playerPlayedCards.size() == 2){
        Persona temp = new Persona(null,false);
        temp.setCards(playerPlayedCards);
        if(temp.calculateEnvido() == playersDeclaredEnvido)
          return 0;
      }

      // Searches for 10, 11 or 12 and it keeps the bigger one
      // Ex: 10,11,12 --> 12
      //     11,12    --> 12
      //     11, 10   --> 11

      // If there is 2 or more '10, 11 or 12' in the array of possibilities, keeps the
      // bigger one and deletes the other ones. This is to make the algorithm not take
      // in count those multiple cards, because it can make the probability not
      // precise. Real case escenario: AI determined that the probabilities where
      // 10,11,12 and 3, but AI couldn't win the 3 although the 10, 11 and 12 it could.
      // So the probability was a 75%, but with this function, is 50%
      for(int i=0; i<possibilities.size()-1; i++){
        Carta c1 = possibilities.get(i);
        Carta c2 = possibilities.get(i+1);
        if(c1.getStick().equals(c2.getStick()))
          if(c1.getNumber() >= 10 && c1.getNumber() <= 12 )
            if(c2.getNumber() >= 10 && c2.getNumber() <= 12 ){
              if(c1.rankingCard() > c2.rankingCard())
                possibilities.remove(i+1);
              else
                possibilities.remove(i);
              i--;
            }
      }

      // Searches for how many cards AI can beat
      int cardAiCanWinCount=0;
      for (int i = 0; i < possibilities.size(); i++)
        if(aiLastCard.rankingCard() > possibilities.get(i).rankingCard())
          cardAiCanWinCount++;

      // Calculates the possibility and returns the state
      double possibility;
      try {
        possibility = ((double)cardAiCanWinCount / (double)possibilities.size()) * 100.0;
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la Inteligencia de la PC: " + e.getMessage());
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        return 0;
      }
      if (possibility>80)
        return 1;
      if (possibility>40)
        return 2;
      return 3;
    }
    return 0;
  }
}
