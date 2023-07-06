package truco_java;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SinglePlayer extends GameManagment{
    private Ai aiAlgorithm = new Ai();

    public SinglePlayer(Truco_Java menu, PlayMenu playMenu) {
        super(menu, playMenu);

        opponentNumber = PlayMenu.playerNumber+1;
        loadPlayersName();
        setBackground(0);
        pointsBackground = new JLabel(getImageIcon("src/truco_java/puntaje/bg"+ opponentNumber +".png", 100, 150, false));

        setLayout(null);
        setDefaultCloseOperation(3);

        Thread thread = new Thread(){
            public void run(){
                JOptionPane.showMessageDialog(null, "Aprete el mazo para repartir y comenzar a jugar...");
                effects.setFile("src/truco_java/musica/boton.wav", 1);
                effects.play();
            }
        };
        thread.start();
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
        updatePoints();
        anotherRound();
        updatesTurn();
    }

    protected void envidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(1);
        askAiEnvido();
    }

    protected void envidoEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(2);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        askAiEnvido();
    }

    protected void realEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(3);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        askAiEnvido();
    }

    protected void faltaEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(4);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        askAiEnvido();
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
        updatePoints();
        setBackground(0);
        updatesTurn();
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
        updatePoints();

        updatesTurn();
    }

    protected void trucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        finishedEnvido = true;
        if(enabledToRetrucar != 2){
            trucoLevel++;
            enabledToRetrucar = 2;
            askAiTruco(true);
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
        updatesTurn();
        setBackground(0);
    }

    protected void noQuieroTrucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        opponent.setPoints(opponent.getPoints() + countPointsLoseTruco(), this);
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        anotherRound();
        updatesTurn();
        setBackground(0);
    }

    protected void backAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();

        // If the game hasn't started
        if(dealCards.getActionListeners().length!=0){
            playMenu.setVisible(true);
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

    protected void actionWhenPlayerWinsGame() {
        JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó el Jugador. Felicidades");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        playMenu.setVisible(true);
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

    protected void actionWhenOpponentWinsGame(){
        JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó " + opponentName + ". Será la próxima...");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        playMenu.setVisible(true);
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

    protected void aditionalActionsInAnotherRound(){
        aiAlgorithm.setPlayersDeclaredEnvido(-1);
    }

    protected void actionsIfPlayerWinsRound(){
        JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó el Jugador.");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();

        player.setPoints(player.getPoints() + countPointsWonTruco(), this);
    }

    protected void aditionalActionsInPlayersTurn() { }

    protected void aditionalActionsInOpponentsTurn(){
        // Not call askEnvidoToAI, because it's already inside of askTrucoToAI
        // TODO: Separate Functions Truco and Envido of askAiTruco
        if (askAiTruco(false)==0);
        else return;
        opponent = aiAlgorithm.playTurnAlgorithm(opponent, player.getPlayedCards());
        moveCard(opponent.getCards().size(), opponent.getPlayedCards().size()-1, opponent, false);
    }

    protected void actionsIfOpponentWinsRound(){
        JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó " + opponentName + ".");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();

        opponent.setPoints(opponent.getPoints() + countPointsWonTruco(), this);
    }

    private int askAiEnvido() {
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
    private int askAiTruco(boolean hasToAnswer){
        // If it returns 0, that means not do anything
        // If it returns 1, that means that the user has to response (stop normal
        //  execution of the game till the user responds)
        if (enabledToRetrucar == 1)
            return 0;

        if(opponent.getPlayedCards().size() == 3 && opponent.getPlayedCards().getNotNullCards(2).rankingCard()==0) {
            truco.setEnabled(false);
            return 0;
        }
        if(player.getPlayedCards().size() == 3 && player.getPlayedCards().getNotNullCards(2).rankingCard()==0)
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
}
