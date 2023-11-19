package truco_java;

import java.awt.Image;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ServerMultiplayer extends GameManagment {
    public Server server;
    public Server chat_socket;
    private int port;
    private Chat chat;

    protected void loadPlayersName(){
        Thread waitingRoomThread = new Thread(){
            public void run(){
                // Creates the server and waits to receive the information about the opponent
                try{
                    WaitingRoom waitingRoom = new WaitingRoom(playMenu, port, ServerMultiplayer.this);
                    waitingRoom.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                    waitingRoom.setResizable(false);
                    waitingRoom.setTitle("Esperando Conección");
                    waitingRoom.setBounds(0,0,500,300);
                    waitingRoom.setLocationRelativeTo(null);
                    waitingRoom.setVisible(true);

                    server = new Server("localhost", port);
                    chat_socket = new Server("localhost", port+1);
                    try {
                        exchangePlayersName();
                    } catch (Exception e) {
                        waitingRoom.dispose();
                        dispose();
                        return;
                    }
                    window.setVisible(true);
                    waitingRoom.dispose();

                    setBackground(0);
                    pointsBackground.setIcon(getImageIcon("src/truco_java/puntaje/bg"+ opponentNumber +".png", 100, 150, false));
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    // waitingRoom.dispose();
                    dispose();
                }
            }
        };
        waitingRoomThread.start();
    }

    public ServerMultiplayer(Truco_Java menu, int port, PlayMenu playMenu) throws IOException {
        super(menu,playMenu);
        this.menu = menu;
        this.playMenu = playMenu;
        this.port = port;

        setLayout(null);
        setDefaultCloseOperation(3);

        loadPlayersName();

        JButton chatButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/msgBoton.png")).getScaledInstance(80, 50, Image.SCALE_SMOOTH)));
        chatButton.setBounds(300, 10, 80, 50);
        chatButton.setVisible(true);
        chatButton.setOpaque(false);
        chatButton.setContentAreaFilled(false);
        chatButton.setBorderPainted(false);
        background.add(chatButton);
        chatButton.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            if ( chat == null ) {
                try {
                    chat = new Chat(chat_socket, opponentName);
                    chat.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                    chat.setResizable(false);
                    chat.setTitle("Mensajes - Juego Truco");
                    chat.setBounds(0,0,400,500);
                    chat.setLocationRelativeTo(null);
                    chat.setVisible(false);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al inicializar el menu de chat: " + ex.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
            chat.setVisible(true);
        });
    }

    private void exchangePlayersName() throws IOException{
        String name = "";
        if(Truco_Java.userIndex!=-1){
            name = Truco_Java.userList.get(Truco_Java.userIndex).getName();

            if(name!=null && name.length()>0)
                name = name.substring(0, 1).toUpperCase()+name.substring(1);
        } else {
            switch(PlayMenu.playerNumber+1){
                case 1: name="El Carpincho"; break;
                case 2: name="La Roca"; break;
                case 3: name="Messi"; break;
                case 4: name="El Diego"; break;
                case 5: name="Boris"; break;
                case 6: name="Guido"; break;
            }
        }

        playerName=name;
        server.sendPerson(PlayMenu.playerNumber+1, name);
        decodeMessage(server.receiveMessage());
    }

    protected void irAlMazoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
        try{
            server.printMessage(playerName + " se ha ido al mazo. Repartiendo...");
        } catch(IOException er){
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        ThreadOptionPane("Te has ido al mazo. Repartiendo...");
        int points=0;
        if(!finishedEnvido && opponent.getPlayedCards().isEmpty())
            points++;
        opponent.setPoints(opponent.getPoints()+points+countPointsWonTruco(), this);
        anotherRound();
        updatesTurn();
    }

    protected void envidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(1);
        truco.setEnabled(false);
        trucoLevel=0;
        enabledToRetrucar = 0;
        drawButtons();
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        irAlMazo.setEnabled(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        Thread thread = new Thread(){
            public void run(){
                try{
                    server.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(server.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar o recibir el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void envidoEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(2);
        truco.setEnabled(false);
        trucoLevel=0;
        enabledToRetrucar = 0;
        drawButtons();
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        irAlMazo.setEnabled(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        Thread thread = new Thread(){
            public void run(){
                try{
                    server.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(server.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar o recibir el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void realEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(3);
        truco.setEnabled(false);
        trucoLevel=0;
        enabledToRetrucar = 0;
        drawButtons();
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        irAlMazo.setEnabled(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        Thread thread = new Thread(){
            public void run(){
                try{
                    server.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(server.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar o recibir el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void faltaEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(4);
        truco.setEnabled(false);
        trucoLevel=0;
        enabledToRetrucar = 0;
        drawButtons();
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        irAlMazo.setEnabled(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        Thread thread = new Thread(){
            public void run(){
                try{
                    server.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(server.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar o recibir el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void quieroEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(5);
        finishedEnvido=true;
        Thread thread = new Thread(){
            public void run(){
                try {
                    server.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                } catch (Exception ex) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();

        if (player.calculateEnvido() > opponent.calculateEnvido()) {
            try{
                server.printMessage("Has Perdido. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
            } catch(IOException er){
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
            ThreadOptionPane("Has ganado. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
            player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
        }
        else if (player.calculateEnvido() < opponent.calculateEnvido()) {
            try{
                server.printMessage("Has Ganado. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
            } catch(IOException er){
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
            ThreadOptionPane("Has perdido. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
            opponent.setPoints(opponent.getPoints() + countPointsWonEnvido(player.getPoints()), this);
        }
        else if (player.calculateEnvido() == opponent.calculateEnvido()) {
            if (player.isFirstHand() == true) {
                try{
                    server.printMessage("Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + playerName + " es mano");
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                ThreadOptionPane("Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
            } else {
                try{
                    server.printMessage("Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                ThreadOptionPane("Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + opponentName + " es mano");
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
        trucoLevel=0;
        drawButtons();
        updatePoints();
        try {
            server.sendPoints(player.getPoints(), opponent.getPoints());
        } catch (Exception ex) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        setBackground(0);
        updatesTurn();
        printsEnvidoMessage(0,false);
    }

    protected void noQuieroEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(-1);
        finishedEnvido=true;
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        opponent.setPoints(opponent.getPoints() + countPointsLoserEnvido(), this);
        Thread thread = new Thread(){
            public void run(){
                try {
                    server.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                } catch (Exception ex) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        setBackground(0);
        trucoLevel=0;
        drawButtons();
        updatePoints();
        try {
            server.sendPoints(player.getPoints(), opponent.getPoints());
        } catch (Exception ex) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        updatesTurn();
        printsEnvidoMessage(0,false);
    }

    protected void trucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        if(enabledToRetrucar != 2){
            finishedEnvido = true;
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            truco.setEnabled(false);
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            irAlMazo.setEnabled(false);
            cardPlayer1Enabled=false;
            cardPlayer2Enabled=false;
            cardPlayer3Enabled=false;

            trucoLevel++;
            enabledToRetrucar = 2;
            Thread thread = new Thread(){
                public void run(){
                    try{
                        server.sendTruco(trucoLevel, enabledToRetrucar);
                        decodeMessage(server.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar o recibir el mensaje: " + er.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                }
            };
            thread.start();
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            printsTrucoMessage(0,false);
        }
    }

    protected void quieroTrucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidoMenu.setEnabled(false);
        envido.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        irAlMazo.setEnabled(false);
        finishedEnvido=true;

        Thread thread = new Thread(){
            public void run(){
                try{
                    server.sendTruco(4, enabledToRetrucar);
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();

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

        int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + opponentName + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        if(dialogResult == JOptionPane.YES_OPTION){
            try {
                server.sendWithdraw();
            } catch (Exception ex) {
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            playMenu.setVisible(true);
            dispose();
            server.killServer();
            chat_socket.killServer();
        }
    }

    protected void actionWhenPlayerWinsGame() {
        try {
            server.printMessage("Termino el Juego. Ganó " + playerName + ". Será la próxima...");
            server.sendKill(false);
        } catch (Exception e) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + e.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        JOptionPane.showMessageDialog(null, "Termino el Juego. Has ganado! Felicidades");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        playMenu.setVisible(true);
        finishedGame=true;
        anotherRound();
        dispose();
        server.killServer();
        chat_socket.killServer();
    }

    protected void actionWhenOpponentWinsGame() {
        try {
            server.printMessage("Termino el Juego. Has ganado! Felicidades");
            server.sendKill(true);
        } catch (Exception e) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + e.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó " + opponentName + ". Será la próxima...");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        playMenu.setVisible(true);
        finishedGame=true;
        anotherRound();
        dispose();
        server.killServer();
        chat_socket.killServer();
    }


    protected void aditionalActionsInAnotherRound(){
        if(opponent.isFirstHand() == true)
            syncWithClient(false);
    }

    protected void aditionalActionsInPlayersTurn(){
        syncWithClient(false);
    }

    protected void actionsIfPlayerWinsRound(){
        ThreadOptionPane("Termino la Partida. Ganaste!");
        try{
            server.printMessage("Termino la Partida. Ha ganado " + playerName);
        } catch(IOException er){
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
        player.setPoints(player.getPoints() + countPointsWonTruco(), this);
    }

    protected void actionsIfOpponentWinsRound(){
        ThreadOptionPane("Termino la Partida. Ha ganado " + opponentName + ".");
        try{
            server.printMessage("Termino la Partida. Ganaste!");
        } catch(IOException er){
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
        opponent.setPoints(opponent.getPoints() + countPointsWonTruco(), this);
    }

    protected void aditionalActionsInOpponentsTurn(){
        syncWithClient(true);
        Thread thread = new Thread(){
            public void run(){
                try{
                    decodeMessage(server.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar o recibir el mensaje: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
    }

    public void decodeMessage (String message) {
        message=message.trim();

        if(message.isEmpty())
            return;
        Scanner scanner = new Scanner(message);
        String tag = scanner.next();

        switch(tag){
            case "throw":
                opponent.addPlayedCards(scanner.nextInt());
                moveCard(opponent.getCards().size(), opponent.getPlayedCards().size()-1, opponent, false);
                break;
            case "envido":
                int level = Integer.parseInt(scanner.next());
                trucoLevel = Integer.parseInt(scanner.next());
                enabledToRetrucar = Integer.parseInt(scanner.next());
                setBackground(1);

                printsEnvidoMessage(level, false);
                if(level==-1){
                    player.setPoints(player.getPoints() + countPointsLoserEnvido(), this);
                    finishedEnvido=true;
                    envidoMenu.setEnabled(false);
                    envido.setVisible(false);
                    envidoEnvido.setVisible(false);
                    realEnvido.setVisible(false);
                    faltaEnvido.setVisible(false);
                    quieroEnvido.setVisible(false);
                    noQuieroEnvido.setVisible(false);

                    try {
                        server.sendPoints(player.getPoints(), opponent.getPoints());
                    } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + e.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                    updatesTurn();
                } else if(level<5){
                    envidosDeclared.add(level);
                    cardPlayer1Enabled=false;
                    cardPlayer2Enabled=false;
                    cardPlayer3Enabled=false;
                    irAlMazo.setEnabled(false);
                    truco.setEnabled(false);
                    quieroTruco.setVisible(false);
                    noQuieroTruco.setVisible(false);
                    envidoMenu.setEnabled(false);
                    quieroEnvido.setVisible(true);
                    noQuieroEnvido.setVisible(true);

                    ArrayList<JButton> buttons = new ArrayList<>();
                    buttons.add(envido);
                    buttons.add(envidoEnvido);
                    buttons.add(realEnvido);
                    buttons.add(faltaEnvido);
                    for(int i=0;i<level;i++)
                        buttons.get(i).setVisible(false);
                    for(int i=level;i<4;i++){
                        buttons.get(i).setVisible(true);
                        buttons.get(i).setEnabled(true);
                    }
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                } else {
                    finishedEnvido=true;
                    if (player.calculateEnvido() > opponent.calculateEnvido()) {
                        try{
                            server.printMessage("Has Perdido. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
                        } catch(IOException er){
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            effects.play();
                        }
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                        ThreadOptionPane("Has ganado. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
                        player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
                    }
                    else if (player.calculateEnvido() < opponent.calculateEnvido()) {
                        try{
                            server.printMessage("Has Ganado. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
                        } catch(IOException er){
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            effects.play();
                        }
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                        ThreadOptionPane("Has perdido. " + opponentName + " tenía " + opponent.calculateEnvido() + " de envido.");
                        opponent.setPoints(opponent.getPoints() + countPointsWonEnvido(player.getPoints()), this);
                    }
                    else if (player.calculateEnvido() == opponent.calculateEnvido()) {
                        if (player.isFirstHand() == true) {
                            try{
                                server.printMessage("Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + playerName + " es mano");
                            } catch(IOException er){
                                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                effects.play();
                            }
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                            ThreadOptionPane("Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                            player.setPoints(player.getPoints() + countPointsWonEnvido(opponent.getPoints()), this);
                        } else {
                            try{
                                server.printMessage("Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                            } catch(IOException er){
                                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + er.getMessage());
                                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                effects.play();
                            }
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                            ThreadOptionPane("Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + opponentName + " es mano");
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
                    updatePoints();
                    try {
                        server.sendPoints(player.getPoints(), opponent.getPoints());
                    } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + e.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                    setBackground(0);

                    updatesTurn();
                }
                scanner.close();
                return;
            case "withdraw":
                opponent.setPoints(15, this);
                JOptionPane.showMessageDialog(null, "El oponente " + opponentName + " se ha retirado. Has ganado!");
                dispose();
                playMenu.setVisible(true);
                server.killServer();
                chat_socket.killServer();
                break;
            case "truco":
                int nivelTrucoTemp = Integer.parseInt(scanner.next());
                enabledToRetrucar = Integer.parseInt(scanner.next());

                // If there's no envido declared and it's the first round
                if(!finishedEnvido && player.getPlayedCards().isEmpty())
                    envidoMenu.setEnabled(true);

                printsTrucoMessage(nivelTrucoTemp, false);
                if(nivelTrucoTemp!=4 && nivelTrucoTemp!=-1){
                    quieroTruco.setVisible(true);
                    noQuieroTruco.setVisible(true);
                    trucoLevel=nivelTrucoTemp;
                    if(nivelTrucoTemp==3) truco.setEnabled(false);
                    else truco.setEnabled(true);
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                    drawButtons();
                } else {
                    truco.setEnabled(false);
                    quieroTruco.setVisible(false);
                    noQuieroTruco.setVisible(false);
                    if(nivelTrucoTemp==-1){
                        player.setPoints(player.getPoints() + countPointsLoseTruco(), this);
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                        ThreadOptionPane("" + opponentName + " ha rechazado el Truco. Repartiendo...");
                        try {
                            anotherRound();
                        } catch (Exception e) {
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la nueva partida: " + e.getMessage());
                            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            effects.play();
                        }
                    }
                    try {
                        updatesTurn();
                    } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + e.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                }
                break;
            case "player":
                opponentNumber = Integer.parseInt(scanner.next());
                opponentName="";
                while(scanner.hasNext()){
                    opponentName += scanner.next();
                    if(scanner.hasNext()) opponentName+=" ";
                }

                if(opponentName.isEmpty()){
                    switch(opponentNumber){
                        case 1: opponentName="El Carpincho"; break;
                        case 2: opponentName="La Roca"; break;
                        case 3: opponentName="Messi"; break;
                        case 4: opponentName="El Diego"; break;
                        case 5: opponentName="Boris"; break;
                        case 6: opponentName="Guido"; break;
                    }
                }
                else opponentName = opponentName.substring(0, 1).toUpperCase()+opponentName.substring(1);

                ThreadOptionPane(opponentName + " ha entrado en la sala. Esperando a que el oponente reparta...");
                break;
            case "deck":
                int points=0;
                if(!finishedEnvido && player.getPlayedCards().isEmpty())
                    points++;
                player.setPoints(player.getPoints() + points + countPointsWonTruco(), this);
                ThreadOptionPane(opponentName + " se ha ido al mazo. Repartiendo...");
                anotherRound();
                updatesTurn();
                break;
            default:
                // System.out.println("No se detecto la categoria del mensaje: " + cat);
                // for(int i=0;i<cat.length();i++)
                //     System.out.println(i+": "+cat.charAt(i));

                // Repeats the message without the last characters (it can be garbage from socket)
                String catTemp="";
                for(int i=1;i<tag.length();i++){
                    catTemp+=tag.charAt(i);
                }
                String mensajeTemp = catTemp + " ";
                while(scanner.hasNext())
                    mensajeTemp+=scanner.next() + " ";
                if(!message.equals(" ")) decodeMessage(mensajeTemp);
                break;
        }

        scanner.close();
    }

    private void syncWithClient(boolean opponentTurn){
        try {
            server.updateInfo(player.getCards().size(), player.getPlayedCards(), opponent.getCards(), opponent.getPlayedCards(),trucoLevel, finishedEnvido, enabledToRetrucar, opponentTurn, player.getPoints(), opponent.getPoints(), player.isFirstHand());
        } catch (IOException e) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar la actualización al cliente: " + e.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
    }

    // In most cases, JOptionPane doesn't need to be called from a thread, except where the communication (server-client) has to be immediately and don't be waiting for the user input
    private void ThreadOptionPane(String mensaje){
        Thread thread = new Thread(){
            public void run(){
                JOptionPane.showMessageDialog(null, mensaje);
                effects.setFile("src/truco_java/musica/boton.wav", 1);
                effects.play();
            }
        };
        thread.start();
    }
}
