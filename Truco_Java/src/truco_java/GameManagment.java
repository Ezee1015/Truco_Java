package truco_java;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.io.IOException;

public abstract class GameManagment extends GameInterface{

    protected ArrayList<Card> deck = new ArrayList<>();

    // TODO: make this function not abstract
    protected abstract void updatesTurn() throws IOException;
    protected abstract void aditionalActionsInAnotherRound();


    public GameManagment(Truco_Java menu, PlayMenu playMenu){
        super(menu, playMenu);

        loadDeck();

        dealCards.setEnabled(true);
        dealCards.addActionListener((ActionEvent e) -> {
            dealCards.setEnabled(false);
            anotherRound();
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
        if(player.getPlayedCards().size() != opponent.getPlayedCards().size() || player.getPlayedCards().isEmpty())
            return 0;

        int roundsWonPlayer=0, roundsWonOpponent=0, winner=0;
        boolean tieDefines=false;

        for(int i=0;i<player.getPlayedCards().size();i++){
            if(winner!=0)
                break;

            switch (whoWonRound(i)) {
                case 1:
                    if(tieDefines)
                        winner=1;
                    else
                        roundsWonPlayer++;
                    break;
                case 2:
                    if(tieDefines)
                        winner=2;
                    else
                        roundsWonOpponent++;
                    break;
                case 0:
                    tieDefines=true;

                    // Finds who won in the round before
                    for(int z=i;z>0;z--){
                        if(whoWonRound(z-1)==1) { winner=1; break; }
                        if(whoWonRound(z-1)==2) { winner=2; break; }
                    }
                    break;
            }
        }

        // If the 3 rounds are tied, the one who is first hand wins the game
        if(player.getPlayedCards().size() == 3 && winner==0 && tieDefines){
            if(player.isFirstHand())
                return 1;
            else
                return 2;
        }


        if(!tieDefines){
            if(roundsWonPlayer>=2)
                winner=1;
            if(roundsWonOpponent>=2)
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

    protected void actionAfterThrowingCard(boolean isThePlayer, int posCardThrown){
        drawCards();
        try {
            updatesTurn();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        }
    }

    protected void anotherRound() {
        if(!finishedGame && Truco_Java.musicCheckBox.isSelected() && !menu.fastModeCheckBox.isSelected()) {
            effects.setFile("src/truco_java/musica/otraPartida.wav", 1);
            effects.play();
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ex) {
            }
        }

        // Reloads variables
        trucoLevel = 0;
        envidosDeclared = new ArrayList<>();
        finishedEnvido = false;
        enabledToRetrucar = 0;
        drawButtons();
        truco.setIcon(getImageIcon("src/truco_java/fondos/trucoBoton.png", 155, 60, false));
        truco.setVisible(true);
        setBackground(0);

        // Cleans hands
        player.setCards(new ArrayList<>());
        opponent.setCards(new ArrayList<>());
        player.setPlayedCards(new ArrayList<>());
        opponent.setPlayedCards(new ArrayList<>());

        mixDeck();

        // Hides and resets buttons
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        printsEnvidoMessage(0, false);
        truco.setIcon(getImageIcon("src/truco_java/fondos/trucoBoton.png", 155, 60, false));
        truco.setEnabled(true);

        // Deals
        ArrayList<Card> cards1 = new ArrayList<>();
        cards1.add(deck.get(0));
        cards1.add(deck.get(2));
        cards1.add(deck.get(4));
        ArrayList<Card> cards2 = new ArrayList<>();
        cards2.add(deck.get(1));
        cards2.add(deck.get(3));
        cards2.add(deck.get(5));
        for(int i=0;i<2;i++){
            int temp[] = new int[3];
            temp[0] = 0;
            temp[1] = 1;
            temp[2] = 2;
            if(i==0) player.setPosCards(temp);
            else opponent.setPosCards(temp);
        }

        if (opponent.isFirstHand() == true) {
            opponent.setCards(cards1);
            player.setCards(cards2);
            opponent.setFirstHand(false);
            player.setFirstHand(true);
        } else {
            opponent.setCards(cards2);
            player.setCards(cards1);
            opponent.setFirstHand(true);
            player.setFirstHand(false);
        }

        aditionalActionsInAnotherRound();
        updatePoints();
        drawCards();
    }

}
