package truco_java;

import java.util.ArrayList;
import java.util.Random;

import java.awt.event.ActionEvent;

public abstract class GameManagment extends GameInterface{

    protected ArrayList<Card> deck = new ArrayList<>();

    protected abstract void aditionalActionsInAnotherRound();
    protected abstract void actionsIfPlayerWinsRound();
    protected abstract void actionsIfOpponentWinsRound();
    protected abstract void aditionalActionsInPlayersTurn();
    protected abstract void aditionalActionsInOpponentsTurn();


    public GameManagment(Truco_Java menu, PlayMenu playMenu){
        super(menu, playMenu);

        loadDeck();

        dealCards.addActionListener((ActionEvent e) -> {
            // Remove ActionListener of dealCards
            for(int i=0;i<dealCards.getActionListeners().length;i++)
                dealCards.removeActionListener(dealCards.getActionListeners()[i]);

            anotherRound();
            updatesTurn();
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

    // In a GAME, there is multiple ROUNDS, which inside have 1, 2 or 3 MATCHES
    protected int checksWinnerOfRound(){
        if(player.getPlayedCards().size() != opponent.getPlayedCards().size() || player.getPlayedCards().isEmpty())
            return 0;

        int roundsWonPlayer=0, roundsWonOpponent=0, winner=0;
        boolean tieDefines=false;

        for(int i=0;i<player.getPlayedCards().size();i++){
            if(winner!=0)
                break;

            switch (whoWonMatch(i)) {
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

                    // Finds who won in the first match
                    if(i!=0 && whoWonMatch(0)==1) { winner=1; break; }
                    if(i!=0 && whoWonMatch(0)==2) { winner=2; break; }

                    break;
            }
        }

        // If the 3 rounds are tied, the one who is first hand wins the round
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

    private int whoWonMatch(int roundNumber){
        int rankingJugador = player.getPlayedCards().getNotNullCards(roundNumber).rankingCard();
        int rankingAI = opponent.getPlayedCards().getNotNullCards(roundNumber).rankingCard();

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
        updatesTurn();
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
        player.setCards(new SetOfCards());
        opponent.setCards(new SetOfCards());
        player.setPlayedCards(new SetOfCards());
        opponent.setPlayedCards(new SetOfCards());

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
        SetOfCards cards1 = new SetOfCards();
        cards1.add(deck.get(0));
        cards1.add(deck.get(2));
        cards1.add(deck.get(4));
        SetOfCards cards2 = new SetOfCards();
        cards2.add(deck.get(1));
        cards2.add(deck.get(3));
        cards2.add(deck.get(5));

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

    protected void updatesTurn(){
        if(finishedGame)
            return;
        if(checksWinnerOfRound()==1) {
            actionsIfPlayerWinsRound();

            anotherRound();
            updatesTurn();
            return;
        }
        if(checksWinnerOfRound()==2) {
            actionsIfOpponentWinsRound();

            anotherRound();
            updatesTurn();
            return;
        }

        if (player.getPlayedCards().isEmpty() && opponent.getPlayedCards().isEmpty()) {
            if (player.isFirstHand() == true) {
                if(!finishedEnvido) envidoMenu.setEnabled(true);
                actionsInPlayersTurn();
            } else
                actionsInOpponentTurn();

        } else if (player.getPlayedCards().isEmpty() && !opponent.getPlayedCards().isEmpty()) {
            if(!finishedEnvido) envidoMenu.setEnabled(true);
            actionsInPlayersTurn();

        } else if (!player.getPlayedCards().isEmpty() && opponent.getPlayedCards().isEmpty())
            actionsInOpponentTurn();

        else if (!player.getPlayedCards().isEmpty() && !opponent.getPlayedCards().isEmpty()) {
            // If it's a round that no one has played yet
            if (player.getPlayedCards().size() == opponent.getPlayedCards().size()) {
                int rankingJugador = player.getPlayedCards().getNotNullCards(player.getPlayedCards().size()-1).rankingCard();
                int rankingAI = opponent.getPlayedCards().getNotNullCards(opponent.getPlayedCards().size()-1).rankingCard();
                envidoMenu.setEnabled(false);

                if (rankingJugador > rankingAI)
                    actionsInPlayersTurn();

                else if (rankingAI > rankingJugador)
                    actionsInOpponentTurn();

                else if(rankingJugador == rankingAI){
                    if(player.isFirstHand())
                        actionsInPlayersTurn();

                    else
                        actionsInOpponentTurn();

                }
            } else if (player.getPlayedCards().size() == opponent.getPlayedCards().size() - 1)
                actionsInPlayersTurn();

            else if (player.getPlayedCards().size() - 1 == opponent.getPlayedCards().size())
                actionsInOpponentTurn();

        }
    }

    private void actionsInPlayersTurn(){
        if(enabledToRetrucar < 2 && trucoLevel != 3) truco.setEnabled(true);

        // If the last card that the AI has thrown is a 4, player can't say 'truco'
        if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().getNotNullCards(2).rankingCard()==0) truco.setEnabled(false);

        irAlMazo.setEnabled(true);
        cardPlayer1Enabled=true;
        cardPlayer2Enabled=true;
        cardPlayer3Enabled=true;
        connectionBackground.setIcon(getImageIcon("src/truco_java/fondos/turnoJugador.png", false));

        aditionalActionsInPlayersTurn();
    }

    private void actionsInOpponentTurn(){
        truco.setEnabled(false);
        envidoMenu.setEnabled(false);
        irAlMazo.setEnabled(false);
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;
        connectionBackground.setIcon(getImageIcon("src/truco_java/fondos/turnoOponente.png", false));

        aditionalActionsInOpponentsTurn();
    }

}
