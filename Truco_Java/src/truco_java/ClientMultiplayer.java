package truco_java;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class ClientMultiplayer extends GameInterface {
    private Client client;
    private Client chat_socket;
    private Chat chat;

    public ClientMultiplayer(Truco_Java menu, String ip, int port, PlayMenu playMenu) throws IOException{
        super(menu, playMenu);

        client = new Client(ip, port);
        chat_socket = new Client(ip, port+1);

        loadPlayersName();

        dealCards.setLocation(36,280);
        dealCards.setIcon(getImageIcon("src/truco_java/fondos/cartasMazo.png", 80, 80, true));

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

    protected void loadPlayersName(){
        Thread waitingForName = new Thread(){
            public void run(){
                try{
                    decodeMessage(client.receiveMessage());
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el nombre del Jugador: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                setBackground(0);
                pointsBackground.setIcon(getImageIcon("src/truco_java/puntaje/bg"+ opponentNumber +".png", 100, 150, false));
                try{
                    decodeMessage(client.receiveMessage());
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al recibir el mensaje: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        waitingForName.start();
    }

    protected void irAlMazoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        Thread thread = new Thread(){
            public void run(){
                try{
                    decodeMessage(client.sendIrAlMazo());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        JOptionPane.showMessageDialog(null, "Te has ido al mazo.");
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
    }

    protected void backAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();

        int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + opponentName + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
        effects.play();
        if(dialogResult == JOptionPane.YES_OPTION){
            try {
                client.sendWithdraw();
            } catch (Exception ex) {
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            playMenu.setVisible(true);
            window.dispose();
            if(chat!=null) chat.dispose();
        }
    }

    protected void envidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(1);
        truco.setEnabled(false);
        trucoLevel=0;
        enabledToRetrucar = 0;
        try {
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
        quieroEnvido.setVisible(false);
        noQuieroEnvido.setVisible(false);
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
                    client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
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
        try {
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
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void realEnvidoAction(ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(3);
        truco.setEnabled(false);
        trucoLevel=0;
        enabledToRetrucar = 0;
        try {
            drawButtons();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
        }
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        irAlMazo.setEnabled(false);
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
                    client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
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
        try {
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
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
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
        // The '5' means 'Quiero'. It's going to be processed in the server
        envidosDeclared.add(5);
        truco.setEnabled(false);
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

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void noQuieroEnvidoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        envidosDeclared.add(-1);
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

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        printsEnvidoMessage(0,false);
    }

    protected void trucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        if(enabledToRetrucar != 1){
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
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

            trucoLevel++;
            enabledToRetrucar = 1;
            Thread thread = new Thread(){
                public void run(){
                    try{
                        client.sendTruco(trucoLevel, enabledToRetrucar);
                        decodeMessage(client.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                }
            };
            thread.start();
            try {
                printsTrucoMessage(0, false);
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
        truco.setEnabled(false);
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        irAlMazo.setEnabled(false);
        finishedEnvido=true;
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.sendTruco(4, enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        setBackground(0);
    }

    protected void noQuieroTrucoAction (ActionEvent e) {
        effects.setFile("src/truco_java/musica/boton.wav", 1);
        effects.play();
        quieroTruco.setVisible(false);
        noQuieroTruco.setVisible(false);
        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.sendTruco(-1,enabledToRetrucar);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
        setBackground(0);
    }

    protected void actionAfterThrowingCard(boolean isThePlayer, int posCardThrown){
        drawCards();

        if(!isThePlayer)
            return;

        Thread thread = new Thread(){
            public void run(){
                try{
                    client.throwCard(posCardThrown);
                    decodeMessage(client.receiveMessage());
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
            }
        };
        thread.start();
    }

    protected void actionWhenPlayerWinsGame() {}
    protected void actionWhenOpponentWinsGame() {}

    public void decodeMessage (String message) {
        if(message==""){
            try {
                decodeMessage(client.receiveMessage());
            } catch (Exception e) {
                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + e.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            return;
        }
        Scanner scanf = new Scanner(message.trim());
        String cat="";
        if(scanf.hasNext()) cat = scanf.next();
        else {
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la comunicación. Mensaje corrupto");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            Thread thread = new Thread(){
                public void run(){
                    try{
                        decodeMessage(client.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                }
            };
            thread.start();
            return;
        }

        switch(cat){
            case "update":
                boolean moveCardOpponent=false;
                int opponentCardsCount = scanf.nextInt();
                if(opponentCardsCount < opponent.getCards().size())
                    moveCardOpponent = true;

                // Fills 'cards' of the opponent with empty cards of the amount given
                SetOfCards cardsOpponent = new SetOfCards();
                for(int i=0; i<opponentCardsCount; i++)
                    cardsOpponent.add(new Card(0, ""));
                opponent.setCards(cardsOpponent);

                SetOfCards playedCardsOpponent = new SetOfCards();
                for(int i=0;i<3-opponentCardsCount;i++){
                    try{
                        String number = scanf.next();
                        String stick = scanf.next();
                        playedCardsOpponent.add(i, new Card(Integer.parseInt(number), stick));
                    } catch(NumberFormatException e){
                        playedCardsOpponent.add(i, null);
                    }
                }
                opponent.setPlayedCards(playedCardsOpponent);

                SetOfCards cards = new SetOfCards();
                for(int i=0;i<3;i++){
                    try{
                        String number = scanf.next();
                        String stick = scanf.next();
                        cards.add(i, new Card(Integer.parseInt(number), stick));
                    } catch(NumberFormatException e){
                        cards.add(i, null);
                    }
                }
                player.setCards(cards);

                SetOfCards playedCards = new SetOfCards();
                for(int i=0;i<3;i++){
                    try {
                        String number = scanf.next();
                        String stick = scanf.next();
                        playedCards.add(i, new Card(Integer.parseInt(number), stick));
                    } catch(NumberFormatException e){
                        playedCards.add(i, null);
                    }
                }
                player.setPlayedCards(playedCards);

                trucoLevel=Integer.parseInt(scanf.next());
                finishedEnvido=Boolean.parseBoolean(scanf.next());
                enabledToRetrucar=Integer.parseInt(scanf.next());

                if(moveCardOpponent)
                    moveCard(opponent.getCards().size(), opponent.getPlayedCards().size()-1, opponent, false);

                // It's player's turn
                if(Boolean.parseBoolean(scanf.next())){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
                    cardPlayer1Enabled=true;
                    cardPlayer2Enabled=true;
                    cardPlayer3Enabled=true;

                    try {
                        drawButtons();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }

                    if((enabledToRetrucar==2 || enabledToRetrucar==0) && trucoLevel!=3)
                        truco.setEnabled(true);
                    else
                        truco.setEnabled(false);
                    // If the last card is 4, disable 'truco'
                    if(opponent.getPlayedCards().size() == 3){
                        if(opponent.getPlayedCards().getNotNullCards(2).rankingCard()==0) {
                            truco.setEnabled(false);
                        }
                    }

                    if(player.getPlayedCards().isEmpty() && !finishedEnvido)
                        envidoMenu.setEnabled(true);
                    else
                        envidoMenu.setEnabled(false);

                    irAlMazo.setEnabled(true);
                    // Opponent's turn
                } else{
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
                    cardPlayer1Enabled=false;
                    cardPlayer2Enabled=false;
                    cardPlayer3Enabled=false;
                    truco.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    envidoMenu.setEnabled(false);

                    // Waits for the server
                    Thread thread = new Thread(){
                        public void run(){
                            try{
                                decodeMessage(client.receiveMessage());
                            } catch(IOException er){
                                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                effects.play();
                            }
                        }
                    };
                    thread.start();
                }

                opponent.setPoints(Integer.parseInt(scanf.next()), this);
                player.setPoints(Integer.parseInt(scanf.next()), this);

                boolean isOpponentFirstHand = scanf.nextBoolean();
                opponent.setFirstHand(isOpponentFirstHand);
                player.setFirstHand(!isOpponentFirstHand);

                if(!moveCardOpponent)
                    drawCards();

                try {
                    updatePoints();
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                break;
            case "envido":
                int level = Integer.parseInt(scanf.next());
                trucoLevel = Integer.parseInt(scanf.next());
                enabledToRetrucar = Integer.parseInt(scanf.next());
                printsEnvidoMessage(level, false);
                setBackground(1);

                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                irAlMazo.setEnabled(false);
                truco.setEnabled(false);
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                envidoMenu.setEnabled(false);

                ArrayList<JButton> buttons = new ArrayList<>();
                buttons.add(envido);
                buttons.add(envidoEnvido);
                buttons.add(realEnvido);
                buttons.add(faltaEnvido);

                if(level==5 || level==-1){
                    quieroEnvido.setVisible(false);
                    noQuieroEnvido.setVisible(false);
                    envidoMenu.setEnabled(false);
                    for(int i=0;i<buttons.size();i++)
                        buttons.get(i).setVisible(false);
                    Thread thread = new Thread(){
                        public void run(){
                            try{
                                decodeMessage(client.receiveMessage());
                            } catch(IOException er){
                                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                effects.play();
                            }
                        }
                    };
                    thread.start();
                    return;
                }

                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));

                for(int i=0;i<level;i++)
                    buttons.get(i).setVisible(false);
                for(int i=level;i<4;i++){
                    buttons.get(i).setVisible(true);
                    buttons.get(i).setEnabled(true);
                }
                quieroEnvido.setVisible(true);
                noQuieroEnvido.setVisible(true);
                break;
            case "print":
                String textToPrint="";
                while(scanf.hasNext()){
                    textToPrint+=scanf.next();
                    if(scanf.hasNext()) textToPrint+= " ";
                }
                final String imprime = textToPrint;
                Thread thread2 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, imprime);
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                };
                thread2.start();
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            decodeMessage(client.receiveMessage());
                        } catch(IOException er){
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            effects.play();
                        }
                    }
                };
                thread.start();
                break;
            case "kill":
                if (scanf.nextBoolean()) //true when the player won
                  player.setPoints(15, this);
                else
                  opponent.setPoints(15, this);
                updatePoints();

                playMenu.setVisible(true);
                dispose();
                if(chat!=null) chat.dispose();
                break;
            case "truco":
                int newTrucoLevel = Integer.parseInt(scanf.next());
                enabledToRetrucar = Integer.parseInt(scanf.next());

                if(!finishedEnvido && player.getPlayedCards().isEmpty())
                    envidoMenu.setEnabled(true);

                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                try {
                    printsTrucoMessage(newTrucoLevel, false);
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                if(newTrucoLevel!=4 && newTrucoLevel!=-1){
                    quieroTruco.setVisible(true);
                    noQuieroTruco.setVisible(true);
                    trucoLevel=newTrucoLevel;
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
                    if(newTrucoLevel==-1){
                        Thread thread3 = new Thread(){
                            public void run(){
                                JOptionPane.showMessageDialog(null, "" + opponentName + " ha rechazado el Truco. Repartiendo...");
                                effects.setFile("src/truco_java/musica/boton.wav", 1);
                                effects.play();
                            }
                        };
                        thread3.start();
                    }
                    Thread thread1 = new Thread(){
                        public void run(){
                            try{
                                decodeMessage(client.receiveMessage());
                            } catch(IOException er){
                                connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                effects.play();
                            }
                        }
                    };
                    thread1.start();
                }
                break;
            case "points":
                opponent.setPoints(Integer.parseInt(scanf.next()), this);
                player.setPoints(Integer.parseInt(scanf.next()), this);
                try {
                    updatePoints();
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                Thread thread3 = new Thread(){
                    public void run(){
                        try{
                            decodeMessage(client.receiveMessage());
                        } catch(IOException er){
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            effects.play();
                        }
                    }
                };
                thread3.start();
                break;
            case "withdraw":
                JOptionPane.showMessageDialog(null, "El oponente " + opponentName + " se ha retirado. Has ganado!");
                playMenu.setVisible(true);
                dispose();
                if(chat!=null) chat.dispose();
                break;
            case "player":
                opponentNumber = Integer.parseInt(scanf.next());
                opponentName="";
                while(scanf.hasNext()){
                    opponentName += scanf.next();
                    if(scanf.hasNext()) opponentName+=" ";
                }
                String nombre = "";
                if(Truco_Java.userIndex!=-1)
                    nombre = Truco_Java.userList.get(Truco_Java.userIndex).getName();

                Thread thread1 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, "Has entrado a la sala de " + opponentName + ". Esperando a que el oponente reparta...");
                        effects.setFile("src/truco_java/musica/boton.wav", 1);
                        effects.play();
                    }
                };
                thread1.start();
                try {
                    client.sendPerson(PlayMenu.playerNumber+1, nombre);
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + e.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                }
                pointsBackground.setIcon(getImageIcon("src/truco_java/puntaje/bg"+ opponentNumber +".png", 100, 150, false));
                break;
            default:
                // System.out.println("No se detecto la categoria del mensaje: " + cat);
                // for(int i=0;i<cat.length();i++)
                //     System.out.println(i+": "+cat.charAt(i));

                // Repeats the message without the last characters (it can be garbage from socket)
                String catTemp="";
                for(int i=1;i<cat.length();i++){
                    catTemp+=cat.charAt(i);
                }
                String mensajeTemp = catTemp + " ";
                while(scanf.hasNext())
                    mensajeTemp+=scanf.next() + " ";
                if(!message.equals(" ")) decodeMessage(mensajeTemp);
                break;
        }
        scanf.close();
    }
}
