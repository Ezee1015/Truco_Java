package truco_java;

import java.io.IOException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ServerMultiplayer extends GameManagment {
    private Server server;
    private int port;

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
                    pointsBackground.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ opponentNumber +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
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
            server.sendMessage("imprimir " + playerName + " se ha ido al mazo. Repartiendo...");
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
        truco.setEnabled(false);
        try {
            trucoLevel=0;
            drawButtons();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
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

        trucoLevel=0;
        enabledToRetrucar = 0;

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
        try {
            trucoLevel=0;
            drawButtons();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
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

        trucoLevel=0;
        enabledToRetrucar = 0;

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
        try {
            trucoLevel=0;
            drawButtons();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
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

        trucoLevel=0;
        enabledToRetrucar = 0;

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
        try {
            trucoLevel=0;
            drawButtons();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
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

        trucoLevel=0;
        enabledToRetrucar = 0;

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
                server.sendMessage("imprimir Has Perdido. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
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
                server.sendMessage("imprimir Has Ganado. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
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
                    server.sendMessage("imprimir Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + playerName + " es mano");
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
                    server.sendMessage("imprimir Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
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
        try {
            trucoLevel=0;
            drawButtons();
            updatePoints();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el dibujar el puntaje: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        try {
            server.sendPoints(player.getPoints(), opponent.getPoints());
        } catch (Exception ex) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
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
        try {
            trucoLevel=0;
            drawButtons();
            updatePoints();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar los puntajes: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        try {
            server.sendPoints(player.getPoints(), opponent.getPoints());
        } catch (Exception ex) {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
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
            try {
                printsTrucoMessage(0,false);
            } catch (Exception ex) {
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
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

        int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + opponentName + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        if(dialogResult == JOptionPane.YES_OPTION){
            try {
                server.sendMessage("retira");
            } catch (Exception ex) {
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            playMenu.setVisible(true);
            dispose();
            server.killServer();
        }
    }

    protected void actionWhenPlayerWins() throws IOException {
        try {
            server.sendMessage("imprimir Termino el Juego. Ganó " + playerName + ". Será la próxima...");
            server.enviaKill();
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
    }

    protected void actionWhenOpponentWins() throws IOException {
        try {
            server.sendMessage("imprimir Termino el Juego. Has ganado! Felicidades");
            server.enviaKill();
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
    }

    protected void anotherRound() throws IOException {
        if(!finishedGame && Truco_Java.musicCheckBox.isSelected() && !menu.fastModeCheckBox.isSelected()) {
            effects.setFile("src/truco_java/musica/otraPartida.wav", 1);
            effects.play();
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerMultiplayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Reloads variables
        trucoLevel = 0;
        envidosDeclared = new ArrayList<>();
        finishedEnvido = false;
        enabledToRetrucar = 0;
        drawButtons();
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
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
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
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
            syncWithClient(false);
        } else {
            opponent.setCards(cards2);
            player.setCards(cards1);
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
            server.sendMessage("imprimir Termino la Partida. Ha ganado " + playerName);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
            ThreadOptionPane("Termino la Partida. Ganaste!");

            player.setPoints(player.getPoints() + countPointsWonTruco(), this);
            anotherRound();
            updatesTurn();
            return;
        }
        if(checksWinnerOfGame()==2) {
            server.sendMessage("imprimir Termino la Partida. Ganaste!");
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
            ThreadOptionPane("Termino la Partida. Ha ganado " + opponentName + ".");

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
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
            } else {
                truco.setEnabled(false);
                envidoMenu.setEnabled(false);
                irAlMazo.setEnabled(false);
                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                syncWithClient(true);
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
        } else if (player.getPlayedCards().isEmpty() && !opponent.getPlayedCards().isEmpty()) {
            if(enabledToRetrucar < 2) truco.setEnabled(true);
            if(!finishedEnvido) envidoMenu.setEnabled(true);
            irAlMazo.setEnabled(true);
            cardPlayer1Enabled=true;
            cardPlayer2Enabled=true;
            cardPlayer3Enabled=true;
            syncWithClient(false);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
        } else if (!player.getPlayedCards().isEmpty() && opponent.getPlayedCards().isEmpty()) {
            truco.setEnabled(false);
            envidoMenu.setEnabled(false);
            irAlMazo.setEnabled(false);
            cardPlayer1Enabled=false;
            cardPlayer2Enabled=false;
            cardPlayer3Enabled=false;
            syncWithClient(true);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
        } else if (!player.getPlayedCards().isEmpty() && !opponent.getPlayedCards().isEmpty()) {
            // If it's a round that nobody played
            if (player.getPlayedCards().size() == opponent.getPlayedCards().size()) {
                int rankingJugador = player.getPlayedCards().get(player.getPlayedCards().size()-1).rankingCard();
                int rankingAI = opponent.getPlayedCards().get(opponent.getPlayedCards().size()-1).rankingCard();
                envidoMenu.setEnabled(false);

                if (rankingJugador > rankingAI) {
                    if(enabledToRetrucar < 2) truco.setEnabled(true);  // If the player can say 'truco'
                                                                       // If the last card that the AI has thrown is a 4, player can't say 'truco'
                    if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                    irAlMazo.setEnabled(true);
                    cardPlayer1Enabled=true;
                    cardPlayer2Enabled=true;
                    cardPlayer3Enabled=true;
                    syncWithClient(false);
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
                } else if (rankingAI > rankingJugador) {
                    truco.setEnabled(false);
                    envidoMenu.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    cardPlayer1Enabled=false;
                    cardPlayer2Enabled=false;
                    cardPlayer3Enabled=false;
                    syncWithClient(true);
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
                } else if(rankingJugador == rankingAI){
                    if(player.isFirstHand()){
                        if(enabledToRetrucar < 2) truco.setEnabled(true);
                        // If the last card that the AI has thrown is a 4, player can't say 'truco'
                        if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                        irAlMazo.setEnabled(true);
                        cardPlayer1Enabled=true;
                        cardPlayer2Enabled=true;
                        cardPlayer3Enabled=true;
                        syncWithClient(false);
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
                    } else {
                        truco.setEnabled(false);
                        envidoMenu.setEnabled(false);
                        irAlMazo.setEnabled(false);
                        cardPlayer1Enabled=false;
                        cardPlayer2Enabled=false;
                        cardPlayer3Enabled=false;
                        syncWithClient(true);
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
                }
            } else if (player.getPlayedCards().size() == opponent.getPlayedCards().size() - 1) {
                if(enabledToRetrucar < 2) truco.setEnabled(true);
                // If the last card that the AI has thrown is a 4, player can't say 'truco'
                if(opponent.getPlayedCards().size() == 3) if(opponent.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                irAlMazo.setEnabled(true);
                cardPlayer1Enabled=true;
                cardPlayer2Enabled=true;
                cardPlayer3Enabled=true;
                syncWithClient(false);
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
            } else if (player.getPlayedCards().size() - 1 == opponent.getPlayedCards().size()) { // Si ya el jugador tiró en esa ronda
                truco.setEnabled(false);
                envidoMenu.setEnabled(false);
                irAlMazo.setEnabled(false);
                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                syncWithClient(true);
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            }
        }
    }

    public void decodeMessage (String message) {
        message=message.trim();

        if(message.isEmpty())
            return;
        Scanner scanner = new Scanner(message);
        String tag = scanner.next();

        switch(tag){
            case "tira":
                int pos = Integer.parseInt(scanner.next());
                opponent.addPlayedCards(opponent.getPosCards()[pos]);
                // Array that indicates that the card should be drawn or not
                int temp[] = opponent.getPosCards();
                temp[pos] = -1;
                for(int i=pos+1;i<temp.length;i++)
                    temp[i]-=1;
                opponent.setPosCards(temp);
                try {
                    moveCard(opponent.getCards().size(), opponent.getPlayedCards().size()-1, opponent, false);
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
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
                    try {
                        updatesTurn();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
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
                            server.sendMessage("imprimir Has Perdido. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
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
                            server.sendMessage("imprimir Has Ganado. " + playerName + " tenía " + player.calculateEnvido() + " de envido.");
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
                                server.sendMessage("imprimir Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + playerName + " es mano");
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
                                server.sendMessage("imprimir Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
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
                    try {
                        updatePoints();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el dibujar el puntaje: " + ex.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                    try {
                        server.sendPoints(player.getPoints(), opponent.getPoints());
                    } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + e.getMessage());
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
                scanner.close();
                return;
            case "retira":
                opponent.setPoints(15, this);
                JOptionPane.showMessageDialog(null, "El oponente " + opponentName + " se ha retirado. Has ganado!");
                dispose();
                playMenu.setVisible(true);
                server.killServer();
                break;
            case "truco":
                int nivelTrucoTemp = Integer.parseInt(scanner.next());
                enabledToRetrucar = Integer.parseInt(scanner.next());

                // If there's no envido declared and it's the first round
                if(!finishedEnvido && player.getPlayedCards().isEmpty())
                    envidoMenu.setEnabled(true);

                try {
                    printsTrucoMessage(nivelTrucoTemp, false);
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                if(nivelTrucoTemp!=4 && nivelTrucoTemp!=-1){
                    quieroTruco.setVisible(true);
                    noQuieroTruco.setVisible(true);
                    trucoLevel=nivelTrucoTemp;
                    if(nivelTrucoTemp==3) truco.setEnabled(false);
                    else truco.setEnabled(true);
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                    try {
                        drawButtons();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + e.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
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
            case "persona":
                opponentNumber = Integer.parseInt(scanner.next());
                opponentName="";
                while(scanner.hasNext()){
                    String nombreTemp = scanner.next();
                    if(!nombreTemp.equals("ç")) opponentName += nombreTemp;
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

                ThreadOptionPane("Has entrado a la sala de " + opponentName + ".\n Aprete el mazo para repartir y comenzar a jugar...");
                break;
            case "mazo":
                int points=0;
                if(!finishedEnvido && player.getPlayedCards().isEmpty())
                    points++;
                player.setPoints(player.getPoints() + points + countPointsWonTruco(), this);
                ThreadOptionPane(opponentName + " se ha ido al mazo. Repartiendo...");
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
            server.updateInfo(player.getCards().size(), player.getPlayedCards(), opponent.getCards(), opponent.getPosCards(), opponent.getPlayedCards(),trucoLevel, finishedEnvido, enabledToRetrucar, opponentTurn, player.getPoints(), opponent.getPoints());
        } catch (IOException e) {
            for(int i=0;i<30;i++){
                try {
                    Thread.sleep(500);
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    server.updateInfo(player.getCards().size(), player.getCards(), opponent.getCards(), opponent.getPosCards(), opponent.getPlayedCards(), trucoLevel, finishedEnvido, enabledToRetrucar, opponentTurn, player.getPoints(), opponent.getPoints());
                    break;
                } catch (Exception er) {}
            }
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
