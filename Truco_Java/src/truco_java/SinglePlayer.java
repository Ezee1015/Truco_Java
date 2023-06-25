package truco_java;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SinglePlayer extends GameManagment{
    private Ai aiAlgorithm = new Ai();

    public SinglePlayer(Truco_Java menu, PlayMenu playMenu) throws IOException {
        super(menu, playMenu);

        opponentNumber = PlayMenu.playerNumber+1;
        loadPlayersName();
        setBackground(0);

        loadDeck();

        setLayout(null);
        setDefaultCloseOperation(3);
    }

    protected void loadPlayersName(){
        switch(opponentNumber){
            case 1: opponentName="El Carpincho"; break;
            case 2: opponentName="La Roca"; break;
            case 3: opponentName="Messi"; break;
            case 4: opponentName="El Diego"; break;
            case 5: opponentName="Boris"; break;
            case 6: opponentName="Guido"; break;
        }
    }

    protected void irAlMazoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        JOptionPane.showMessageDialog(null, "Te has ido al mazo. Repartiendo...");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        int points=0;
        if(!finishedEnvido && opponent.getPlayedCards().isEmpty())
            points++;
        opponent.setPoints(opponent.getPoints()+points+countPointsWonTruco(), this);
        try {
            updatePoints();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar el puntaje: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
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
    }

    protected void envidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(1);
        try {
            askAiEnvido();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + opponentName + ": " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    protected void envidoEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(2);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        try {
            askAiEnvido();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + opponentName + ": " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    protected void realEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(3);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        try {
            askAiEnvido();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + opponentName + ": " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    protected void faltaEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(4);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        try {
            askAiEnvido();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + opponentName + ": " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    protected void quieroEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        if (player.calculateEnvido() > opponent.calculateEnvido()) {
            aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
            JOptionPane.showMessageDialog(null, "Has ganado. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
        }
        else if (player.calculateEnvido() < opponent.calculateEnvido()) {
            aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
            JOptionPane.showMessageDialog(null, "Has perdido. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            opponent.setPoints(opponent.getPoints() + countPointsWonEnvido(player.getPoints()), this);
        }
        else if (player.calculateEnvido() == opponent.calculateEnvido()) {
            if (player.isFirstHand() == true) {
                aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
                JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
            } else {
                aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
                JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + opponentName + " es mano");
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                opponent.setPoints(opponent.getPoints() + countPointsWonEnvido(player.getPoints()), this);
            }
        }
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        cardPlayer1Enabled=true;
        cardPlayer2Enabled=true;
        cardPlayer3Enabled=true;
        try {
            updatePoints();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el dibujar el puntaje: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        setBackground(0);

        try {
            updatesTurn();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    protected void noQuieroEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        opponent.setPoints(opponent.getPoints() + countPointsLoserEnvido(), this);

        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        setBackground(0);
        try {
            updatePoints();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar los puntajes: " + ex.getMessage());
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
    }

    protected void trucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        finishedEnvido = true;
        if(enabledToRetrucar != 2){
            trucoLevel++;
            enabledToRetrucar = 2;
            try {
                askAiTruco(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el truco de " + opponentName + ": " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
        }
    }

    protected void quieroTrucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        cardPlayer1Enabled=true;
        cardPlayer2Enabled=true;
        cardPlayer3Enabled=true;

        if(trucoLevel == 3)
            truco.setEnabled(false);
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        try {
            updatesTurn();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        setBackground(0);
    }

    protected void noQuieroTrucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        opponent.setPoints(opponent.getPoints() + countPointsLoseTruco(), this);
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
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
        setBackground(0);
    }

    protected void backAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();

        // If the game hasn't started
        if(dealCards.isEnabled()){
            menu.setVisible(true);
            finishedGame=true;
            dispose();
            return;
        }

        // If the game has started
        int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + opponentName + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        if(dialogResult == JOptionPane.YES_OPTION)
            opponent.setPoints(15, this);
    }

    public void updatePoints() throws IOException {
        if(finishedGame)
            return;
        int jugadorPunt = player.getPoints();
        int aiPunt = opponent.getPoints();
        if(jugadorPunt > 15)
            jugadorPunt = 15;
        if(aiPunt > 15)
            aiPunt = 15;

        pointsPlayer.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + jugadorPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));
        pointsAi.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + aiPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));


        if(jugadorPunt==15){
            JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó el Jugador. Felicidades");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            menu.setVisible(true);
            finishedGame=true;
            anotherRound();
            if(!menu.easyCheckBox.isSelected()) {
                Truco_Java.gamesWonPlayer++;
                Truco_Java.playerPoints.setText(Integer.toString(Truco_Java.gamesWonPlayer));
                Truco_Java.pointsBackground.setVisible(true);
            }

            // Actualiza los partidos según la sesión
            if(Truco_Java.userIndex!=-1){
                Truco_Java.userList.get(Truco_Java.userIndex).encryptPoints();
                Truco_Java.userList.get(Truco_Java.userIndex).saveChanges(false, 0);
            }

            dispose();
        }
        if(aiPunt==15){

            JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó " + opponentName + ". Será la próxima...");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            menu.setVisible(true);
            finishedGame=true;
            anotherRound();
            if(!menu.easyCheckBox.isSelected()) {
                Truco_Java.gamesWonAi++;
                Truco_Java.AiPoints.setText(Integer.toString(Truco_Java.gamesWonAi));
                Truco_Java.pointsBackground.setVisible(true);
            }

            // If the user is logged in
            if(Truco_Java.userIndex!=-1){
                Truco_Java.userList.get(Truco_Java.userIndex).encryptPoints();
                Truco_Java.userList.get(Truco_Java.userIndex).saveChanges(false, 0);
            }

            dispose();
        }
    }

    protected void throwCard(int pos) throws IOException {
        // Intenta solucionar un bug de diferentes llamados que pueden perjudicar a la mano
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        if(!menu.fastModeCheckBox.isSelected()) moveCardPlayer(pos, player.getCards().get(player.getPosCards()[pos]).linkCard(), player.getPlayedCards().size());

        //Tira la carta
        player.addPlayedCards(player.getPosCards()[pos]);

        // Indica que carta no se debbe dibujar
        int temp[] = player.getPosCards();
        temp[pos] = -1;
        for(int i=pos+1;i<temp.length;i++)
            temp[i]-=1;
        player.setPosCards(temp);

        if(menu.fastModeCheckBox.isSelected()){ //Si no se quiere movimiento de cartas
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

    protected void anotherRound() throws IOException {
        if(!finishedGame && Truco_Java.musicCheckBox.isSelected() && !menu.fastModeCheckBox.isSelected()) {
            effects.setFile("src/truco_java/musica/otraPartida.wav", 1);
            effects.play();
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ex) {
                Logger.getLogger(SinglePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Restart variables
        trucoLevel = 0;
        envidosDeclared = new ArrayList<>();
        finishedEnvido = false;
        enabledToRetrucar = 0;
        drawButtons();
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setVisible(true);
        setBackground(0);
        aiAlgorithm.setPlayersDeclaredEnvido(-1);

        // Clear hands
        player.setCards(new ArrayList<>());
        opponent.setCards(new ArrayList<>());
        player.setPlayedCards(new ArrayList<>());
        opponent.setPlayedCards(new ArrayList<>());

        mixDeck();

        // Hides and restarts buttons
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        printsEnvidoMessage(0, false);
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setEnabled(true);

        // deals cards
        ArrayList<Card> hand1 = new ArrayList<>();
        hand1.add(deck.get(0));
        hand1.add(deck.get(2));
        hand1.add(deck.get(4));
        ArrayList<Card> hand2 = new ArrayList<>();
        hand2.add(deck.get(1));
        hand2.add(deck.get(3));
        hand2.add(deck.get(5));
        int tempPosCards[] = new int[3];
        tempPosCards[0] = 0;
        tempPosCards[1] = 1;
        tempPosCards[2] = 2;
        player.setPosCards(tempPosCards);

        if (opponent.isFirstHand() == true) {
            opponent.setCards(hand1);
            player.setCards(hand2);
            opponent.setFirstHand(false);
            player.setFirstHand(true);
        } else {
            opponent.setCards(hand2);
            player.setCards(hand1);
            opponent.setFirstHand(true);
            player.setFirstHand(false);
        }

        updatePoints();
        drawCards();

    }

    protected void updatesTurn() throws IOException {
        if(finishedGame)
            return;
        if(checksWinnerOfGame()==1) {
            JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó el Jugador.");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();

            player.setPoints(player.getPoints() + countPointsWonTruco(), this);
            anotherRound();
            updatesTurn();
            return;
        }
        if(checksWinnerOfGame()==2) {
            JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó " + opponentName + ".");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();

            opponent.setPoints(opponent.getPoints() + countPointsWonTruco(), this);
            anotherRound();
            updatesTurn();
            return;
        }

        if (player.getPlayedCards().isEmpty() && opponent.getPlayedCards().isEmpty()) {
            if (player.isFirstHand() == true) {
                if(enabledToRetrucar < 2) truco.setEnabled(true);
                if(!finishedEnvido) envidoMenu.setEnabled(true);
                irAlMazo.setEnabled(true);
                cardPlayer1Enabled=true;
                cardPlayer2Enabled=true;
                cardPlayer3Enabled=true;
            } else {
                truco.setEnabled(false);
                envidoMenu.setEnabled(false);
                irAlMazo.setEnabled(false);
                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                // Not call askEnvidoToAI, because it's already inside of askTrucoToAI
                // TODO: Separate Functions
                if (askAiTruco(false)==0);
                else return;
                opponent = aiAlgorithm.playTurnAlgorithm(opponent, player.getPlayedCards());
                moveCardOpponent(opponent.getCards().size(), opponent.getPlayedCards().size()-1);
            }
        } else if (player.getPlayedCards().isEmpty() && !opponent.getPlayedCards().isEmpty()) {
            if(enabledToRetrucar < 2) truco.setEnabled(true);
            if(!finishedEnvido) envidoMenu.setEnabled(true);
            irAlMazo.setEnabled(true);
            cardPlayer1Enabled=true;
            cardPlayer2Enabled=true;
            cardPlayer3Enabled=true;
        } else if (!player.getPlayedCards().isEmpty() && opponent.getPlayedCards().isEmpty()) {
            truco.setEnabled(false);
            envidoMenu.setEnabled(false);
            irAlMazo.setEnabled(false);
            cardPlayer1Enabled=false;
            cardPlayer2Enabled=false;
            cardPlayer3Enabled=false;
            // Not call askEnvidoToAI, because it's already inside of askTrucoToAI
            if (askAiTruco(false)==0);
            else return;
            opponent = aiAlgorithm.playTurnAlgorithm(opponent, player.getPlayedCards());
            moveCardOpponent(opponent.getCards().size(), opponent.getPlayedCards().size()-1);
        } else if (!player.getPlayedCards().isEmpty() && !opponent.getPlayedCards().isEmpty()) {
            // If it's a round that nobody has played (start of the round)
            if (player.getPlayedCards().size() == opponent.getPlayedCards().size()) {
                int playerRankingLastRound = player.getPlayedCards().get(player.getPlayedCards().size()-1).rankingCard();
                int aiRankingLastRound = opponent.getPlayedCards().get(opponent.getPlayedCards().size()-1).rankingCard();
                envidoMenu.setEnabled(false);

                if (playerRankingLastRound > aiRankingLastRound) {
                    if(enabledToRetrucar < 2) truco.setEnabled(true); // If the player can say 'truco'
                                                                      // If the last card that the AI has thrown is a 4, player can't say 'truco'
                    if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                    irAlMazo.setEnabled(true);
                    cardPlayer1Enabled=true;
                    cardPlayer2Enabled=true;
                    cardPlayer3Enabled=true;
                } else if (aiRankingLastRound > playerRankingLastRound) {
                    truco.setEnabled(false);
                    envidoMenu.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    cardPlayer1Enabled=false;
                    cardPlayer2Enabled=false;
                    cardPlayer3Enabled=false;
                    if (askAiTruco(false)==0);
                    else return;
                    opponent = aiAlgorithm.playTurnAlgorithm(opponent, player.getPlayedCards());
                    moveCardOpponent(opponent.getCards().size(), opponent.getPlayedCards().size()-1);
                } else if(playerRankingLastRound == aiRankingLastRound){
                    if(player.isFirstHand()){
                        if(enabledToRetrucar < 2) truco.setEnabled(true);
                        // If the last card that the AI has thrown is a 4, player can't say 'truco'
                        if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                        irAlMazo.setEnabled(true);
                        cardPlayer1Enabled=true;
                        cardPlayer2Enabled=true;
                        cardPlayer3Enabled=true;
                    } else {
                        truco.setEnabled(false);
                        envidoMenu.setEnabled(false);
                        irAlMazo.setEnabled(false);
                        cardPlayer1Enabled=false;
                        cardPlayer2Enabled=false;
                        cardPlayer3Enabled=false;
                        if (askAiTruco(false)==0);
                        else return;
                        opponent = aiAlgorithm.playTurnAlgorithm(opponent, player.getPlayedCards());
                        moveCardOpponent(opponent.getCards().size(), opponent.getPlayedCards().size()-1);
                    }
                }
            } else if (player.getPlayedCards().size() == opponent.getPlayedCards().size() - 1) {
                if(enabledToRetrucar < 2) truco.setEnabled(true);
                // If the last card that the AI has thrown is a 4, player can't say 'truco'
                if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                irAlMazo.setEnabled(true);
                cardPlayer1Enabled=true;
                cardPlayer2Enabled=true;
                cardPlayer3Enabled=true;
            } else if (player.getPlayedCards().size() - 1 == opponent.getPlayedCards().size()) {
                truco.setEnabled(false);
                envidoMenu.setEnabled(false);
                irAlMazo.setEnabled(false);
                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                if (askAiTruco(false)==0);
                else return;
                opponent = aiAlgorithm.playTurnAlgorithm(opponent, player.getPlayedCards());
                moveCardOpponent(opponent.getCards().size(), opponent.getPlayedCards().size()-1);
            }
        }
    }

    private int askAiEnvido() throws IOException {
        if (finishedEnvido == true)
            return 0;

        int decision;
        if (envidosDeclared.isEmpty())
            decision = aiAlgorithm.envidoAlgorithm(opponent, player, 0, menu);

        else {
            decision = aiAlgorithm.envidoAlgorithm(opponent, player, envidosDeclared.get(envidosDeclared.size() - 1), menu);

            // AI Accepts
            if (decision == envidosDeclared.get(envidosDeclared.size() - 1)) {
                opponentVoice.setFile("src/truco_java/cantos/envido/" + opponentNumber + "5.wav", 1);
                opponentVoice.play();

                if (player.calculateEnvido() > opponent.calculateEnvido()) {
                    aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
                    JOptionPane.showMessageDialog(null, "Has ganado. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
                }
                else if (player.calculateEnvido() < opponent.calculateEnvido()) {
                    aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
                    JOptionPane.showMessageDialog(null, "Has perdido. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    opponent.setPoints(opponent.getPoints() + countPointsWonEnvido(player.getPoints()), this);
                }
                else if (player.calculateEnvido() == opponent.calculateEnvido()) {
                    if (player.isFirstHand() == true) {
                        aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                        player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
                    } else {
                        aiAlgorithm.setPlayersDeclaredEnvido(player.calculateEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + opponentName + " es mano");
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                        opponent.setPoints(opponent.getPoints() + countPointsWonEnvido(player.getPoints()), this);
                    }
                }
                quieroEnvido.setVisible(false);
                noQuieroEnvido.setVisible(false);
                envidoMenu.setEnabled(false);
                envido.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
                cardPlayer1Enabled=true;
                cardPlayer2Enabled=true;
                cardPlayer3Enabled=true;
                if(finishedEnvido==true) updatesTurn();
                updatePoints();
                return 1;
            }
        }

        switch (decision) {
            case 0:
                if(!envidosDeclared.isEmpty()){
                    printsEnvidoMessage(-1, false);
                    player.setPoints(player.getPoints() + countPointsLoserEnvido(), this);
                    finishedEnvido=true;
                    envidoMenu.setEnabled(false);
                    envido.setVisible(false);
                    envidoEnvido.setVisible(false);
                    realEnvido.setVisible(false);
                    faltaEnvido.setVisible(false);
                    quieroEnvido.setVisible(false);
                    noQuieroEnvido.setVisible(false);
                    updatesTurn();
                }
                return 0;
            case 1:
                envidoMenu.setEnabled(false);
                envido.setVisible(false);
                envidoEnvido.setVisible(true);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
                break;
            case 2:
                envidoMenu.setEnabled(false);
                envido.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
                break;
            case 3:
                envidoMenu.setEnabled(false);
                envido.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(true);
                break;
            case 4:
                envidoMenu.setEnabled(false);
                envido.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
                break;
        }

        envidosDeclared.add(decision);
        printsEnvidoMessage(decision, false);

        quieroEnvido.setVisible(true);
        noQuieroEnvido.setVisible(true);

        // Cards not available when envido is pending
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        return 1;
    }

    // The argument 'hasToAnswer' is a boolean that indicates if the user sang 'truco', so, that way the AI can say 'No quiero' to what the user said
    private int askAiTruco(boolean hasToAnswer) throws IOException {
        // If it returns 0, that means not do anything
        // If it returns 1, that means that the user has to response (stop normal
        //  execution of the game till the user responds)
        if (enabledToRetrucar == 1)
            return 0;

        if(opponent.getPlayedCards().size() == 3 && opponent.getPlayedCards().get(2).rankingCard()==0) {
            truco.setEnabled(false);
            return 0;
        }
        if(player.getPlayedCards().size() == 3 && player.getPlayedCards().get(2).rankingCard()==0)
            return 0;

        if(!finishedEnvido && opponent.getPlayedCards().isEmpty()){
            if(askAiEnvido()!=0){
                if(trucoLevel!=0) trucoLevel--;
                enabledToRetrucar = 0;
                return 1;
            }
        }

        int decision = aiAlgorithm.trucoAlgorithm(opponent, trucoLevel, player, menu);

        // If there is no truco declared and the AI doesn't want
        if(decision == trucoLevel && trucoLevel==0)
            return 0;

        // AI doesn't want truco
        if(decision==0){
            if (enabledToRetrucar == 2 && hasToAnswer) {
                printsTrucoMessage(-1, false);
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                player.setPoints(player.getPoints() + countPointsLoseTruco(), this);
                JOptionPane.showMessageDialog(null, "" + opponentName + " ha rechazado el Truco. Repartiendo...");
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                anotherRound();
                updatesTurn();
                return 1;
            } else return 0;
        }

        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        // Accepts truco
        if(decision == trucoLevel && hasToAnswer){
            enabledToRetrucar=2;
            truco.setEnabled(false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);

            cardPlayer1Enabled=true;
            cardPlayer2Enabled=true;
            cardPlayer3Enabled=true;
            printsTrucoMessage(4, false);

            updatesTurn();
            return 0;
        }

        // If AI doesn't have to answer and wants less than the actual offer (this shouldn't be possible)
        if(!hasToAnswer && decision<(trucoLevel+1))
            return 0;
        // If AI has to answer and wants less than the actual offer (this shouldn't be possible)
        if(hasToAnswer && decision<(trucoLevel+1)){
            printsTrucoMessage(-1, false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            player.setPoints(player.getPoints() + countPointsLoseTruco(), this);
            JOptionPane.showMessageDialog(null, "" + opponentName + " ha rechazado el Truco. Repartiendo...");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            anotherRound();
            updatesTurn();
            return 1;
        }

        enabledToRetrucar=1; // user should retrucar or accept
        trucoLevel++;
        printsTrucoMessage(trucoLevel, false);
        quieroTruco.setVisible(true);
        noQuieroTruco.setVisible(true);

        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);

        return 1;
    }

    protected void moveCardTimer (int moveX, int moveY, int origenX, int originY, int destinationX, int destinationY, int width, int height, String file) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        movingCard.setBounds(origenX+moveX,originY+moveY, movingCard.getWidth()-width, movingCard.getHeight()-height);
                        try {
                            movingCard.setIcon(new ImageIcon(ImageIO.read(new File(file)).getScaledInstance(movingCard.getWidth()-width, movingCard.getHeight()-height, Image.SCALE_SMOOTH)));
                        } catch (IOException e) {
                        }
                    }
                },
                1
                );

        try {
            TimeUnit.MILLISECONDS.sleep(6);
        } catch (Exception e) {
        }

        movingCard.repaint();
        if(origenX-destinationX>5 || origenX-destinationX<-5){
            moveCardTimer(moveX,moveY, origenX+moveX, originY+moveY, destinationX, destinationY, width, height, file);
            return;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (Exception e) {
        }


        // Enables to play
        movingCard.setVisible(false);
        background.remove(movingCard);
        try {
            drawCards();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar las cartas: " + ex.getMessage());
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
    }
}
