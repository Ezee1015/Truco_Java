package truco_java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;

public abstract class GameManagment extends GameInterface{

    protected ArrayList<Card> deck = new ArrayList<>();

    // TODO: make this function not abstract
    protected abstract void anotherRound() throws IOException;
    // TODO: make this function not abstract
    protected abstract void updatesTurn() throws IOException;


    public GameManagment(Truco_Java menu, PlayMenu playMenu) throws IOException{
        super(menu, playMenu);

        loadDeck();

        dealCards.setEnabled(true);
        dealCards.addActionListener((ActionEvent e) -> {
            dealCards.setEnabled(false);
            try {
                anotherRound();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de comenzar otra partida: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            try {
                updatesTurn();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        });
    }

    protected void loadDeck() {
        deck.add(new Card(1, "espada"));
        deck.add(new Card(2, "espada"));
        deck.add(new Card(3, "espada"));
        deck.add(new Card(4, "espada"));
        deck.add(new Card(5, "espada"));
        deck.add(new Card(6, "espada"));
        deck.add(new Card(7, "espada"));
        deck.add(new Card(10, "espada"));
        deck.add(new Card(11, "espada"));
        deck.add(new Card(12, "espada"));
        deck.add(new Card(1, "basto"));
        deck.add(new Card(2, "basto"));
        deck.add(new Card(3, "basto"));
        deck.add(new Card(4, "basto"));
        deck.add(new Card(5, "basto"));
        deck.add(new Card(6, "basto"));
        deck.add(new Card(7, "basto"));
        deck.add(new Card(10, "basto"));
        deck.add(new Card(11, "basto"));
        deck.add(new Card(12, "basto"));
        deck.add(new Card(1, "oro"));
        deck.add(new Card(2, "oro"));
        deck.add(new Card(3, "oro"));
        deck.add(new Card(4, "oro"));
        deck.add(new Card(5, "oro"));
        deck.add(new Card(6, "oro"));
        deck.add(new Card(7, "oro"));
        deck.add(new Card(10, "oro"));
        deck.add(new Card(11, "oro"));
        deck.add(new Card(12, "oro"));
        deck.add(new Card(1, "copa"));
        deck.add(new Card(2, "copa"));
        deck.add(new Card(3, "copa"));
        deck.add(new Card(4, "copa"));
        deck.add(new Card(5, "copa"));
        deck.add(new Card(6, "copa"));
        deck.add(new Card(7, "copa"));
        deck.add(new Card(10, "copa"));
        deck.add(new Card(11, "copa"));
        deck.add(new Card(12, "copa"));
    }

    protected void mixDeck() {
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            ArrayList<Card> tempDeck = new ArrayList<>();
            for (int x = 0; x < deck.size() + tempDeck.size(); x++) {
                int mixingPos = random.nextInt(deck.size());
                tempDeck.add(deck.get(mixingPos));
                deck.remove(mixingPos);
            }
            deck.clear();
            deck.addAll(tempDeck);
        }
    }

    protected int countPointsWonEnvido(int looserPoints) {
        int total = 0;

        for (int i = 0; i < envidosDeclared.size(); i++) {
            switch (envidosDeclared.get(i)) {
                case 1:
                    total += 2;
                    break;
                case 2:
                    total += 2;
                    break;
                case 3:
                    total += 3;
                    break;
                case 4:
                    total = 15 - looserPoints;
            }
        }

        finishedEnvido = true;
        return total;
    }

    protected int countPointsLoserEnvido() {
        int total = 0;
        finishedEnvido = true;

        // If there is only one envido declared, gives one point
        if( (envidosDeclared.size() == 1) ||
            // or if there is an 'envido' and a 'no quiero'
            (envidosDeclared.size() == 2 && envidosDeclared.get(envidosDeclared.size()-1)==-1))
            return 1;

        for (int i = 0; i < envidosDeclared.size()-1; i++) {
            switch (envidosDeclared.get(i)) {
                case 1:
                    total += 2;
                    break;
                case 2:
                    total += 2;
                    break;
                case 3:
                    total += 3;
                    break;
            }
        }
        return total;
    }

    protected int checksWinnerOfGame(){
        if(player.getPlayedCards().size() != opponent.getPlayedCards().size() || opponent.getPlayedCards().isEmpty())
            return 0;

        int roundsWonPlayer=0, roundsWonAi=0, winner=0;
        boolean tieDefines=false;

        for(int i=0;i<player.getPlayedCards().size();i++){ // se fija por cada ronda
            if(winner!=0)
                break;

            OUTER:
            OUTER_1:
            switch (i) {
                case 0:
                    switch (whoWonRound(i)) {
                        case 1:
                            roundsWonPlayer++;
                            break;
                        case 2:
                            roundsWonAi++;
                            break;
                        case 0:
                            tieDefines=true;
                            break;
                        default:
                            break;
                    }
                    break;
                case 1:
                    switch (whoWonRound(i)) {
                        case 1:
                            if (tieDefines==true) {
                                winner=1;
                                break OUTER_1;
                            } else {
                                roundsWonPlayer++;
                            }
                            break;
                        case 2:
                            if (tieDefines==true) {
                                winner=2;
                                break OUTER_1;
                            } else {
                                roundsWonAi++;
                            }
                            break;
                        case 0:
                            switch(whoWonRound(0)){
                                case 0:
                                    tieDefines=true;
                                    break;
                                case 1:
                                    winner=1;
                                    break;
                                case 2:
                                    winner=2;
                                    break;
                            }   break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (whoWonRound(i)) {
                        case 1:
                            if (tieDefines==true) {
                                winner=1;
                                break OUTER;
                            } else {
                                roundsWonPlayer++;
                            }
                            break;
                        case 2:
                            if (tieDefines==true) {
                                winner=2;
                                break OUTER;
                            } else {
                                roundsWonAi++;
                            }
                            break;
                        case 0:
                            switch(whoWonRound(0)){
                                case 0:
                                    tieDefines=true; break;
                                case 1:
                                    winner=1;
                                    break;
                                case 2:
                                    winner=2;
                                    break;
                            }   break;
                        default:
                            break;
                    }
                    break;
            }
        }

        // If the 3 rounds are tied, wins the one who is first hand
        if( player.getPlayedCards().size() == 3 && whoWonRound(0)==0 && whoWonRound(1)==0 && whoWonRound(2)==0){
            if(player.isFirstHand())
                return 1;
            else
                return 2;
        }


        if(tieDefines==false && (roundsWonPlayer>=2 || roundsWonAi>=2)){
            if(roundsWonPlayer>roundsWonAi)
                winner=1;
            else
                winner=2;
        }

        return winner;
    }

    private int whoWonRound(int roundNumber){
        int rankingJugador = player.getPlayedCards().get(roundNumber).rankingCard();
        int rankingAI = opponent.getPlayedCards().get(roundNumber).rankingCard();

        if(rankingJugador > rankingAI)
            return 1;
        if(rankingJugador < rankingAI)
            return 2;

        return 0;
    }

    protected int countPointsWonTruco() {
        switch(trucoLevel){
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
        }
        return 0;
    }

    protected int countPointsLoseTruco() {
        switch(trucoLevel-1){
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
        }

        return 0;
    }

    protected void throwCard(int pos) throws IOException {
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        if(!menu.fastModeCheckBox.isSelected())
            moveCardPlayer(pos, player.getCards().get(player.getPosCards()[pos]).linkCard(), player.getPlayedCards().size());

        player.addPlayedCards(player.getPosCards()[pos]);

        // Indicates which card should not be drawn
        int temp[] = player.getPosCards();
        temp[pos] = -1;
        for(int i=pos+1;i<temp.length;i++)
            temp[i]-=1;
        player.setPosCards(temp);

        if(menu.fastModeCheckBox.isSelected()){
            try {
                drawCards();
                updatesTurn();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        }

    }

}
