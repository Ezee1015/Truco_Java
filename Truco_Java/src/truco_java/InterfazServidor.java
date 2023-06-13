package truco_java;

import java.io.IOException;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class InterfazServidor extends JFrame {
    public Servidor server;

    // Swing
    private ArrayList<Carta> deck = new ArrayList<>();
    private JLabel background = new JLabel();
    private Truco_Java menu;
    private MenuJugar playMenu;
    private JLabel cardOpponent1, cardOpponent2, cardOpponent3;
    private JLabel cardThrownOpponent1, cardThrownOpponent2, cardThrownOpponent3;
    private JButton cardPlayer1, cardPlayer2, cardPlayer3;
    private JLabel cardThrownPlayer1, cardThrownPlayer2, cardThrownPlayer3;
    private JButton truco, envidoMenu, irAlMazo, envido, envidoEnvido, realEnvido, faltaEnvido;
    private JButton noQuieroEnvido, quieroEnvido, noQuieroTruco, quieroTruco;
    private JLabel statusBackground;
    private JLabel pointsAi = new JLabel(), pointsPlayer = new JLabel();
    private JButton movingCard = new JButton();
    private boolean cardPlayer1Enabled=false, cardPlayer2Enabled=false, cardPlayer3Enabled=false;
    private JTextPane status;
    private boolean finishedGame = false;
    private JLabel connectionBackground = new JLabel(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
    JLabel pointsBackground;

    // Players
    private Persona player = new Persona(null, false);
    private int opponentNumber; // Represents the player that was selected personaje que fue generado
    private String opponentName = "the PC";
    private String playerName = "the Player";
    private Persona opponent = new Persona(null, true);

    // Information of the game
    private int trucoLevel = 0;
    private ArrayList<Integer> envidosDeclared;
    private boolean finishedEnvido = false;
    private int enabledToRetrucar = 0; // 1--> Player; 2--> Opponent
    private final Music opponentVoice = new Music();
    private static final Music effects = new Music();

    private void exchangePlayersName() throws IOException{
        String name = "";
        if(Truco_Java.userIndex!=-1){
            name = Truco_Java.userList.get(Truco_Java.userIndex).getName();

            if(name!=null && name.length()>0)
                name = name.substring(0, 1).toUpperCase()+name.substring(1);
        } else {
            switch(MenuJugar.playerNumber+1){
                case 1: name="El Carpincho"; break;
                case 2: name="La Roca"; break;
                case 3: name="Messi"; break;
                case 4: name="El Diego"; break;
                case 5: name="Boris"; break;
                case 6: name="Guido"; break;
            }
        }

        playerName=name;
        server.sendPerson(MenuJugar.playerNumber+1, name);
        decodeMessage(server.receiveMessage());
    }

    public InterfazServidor(Truco_Java menu, String ip, int port, MenuJugar playMenu) throws IOException {
        this.menu = menu;
        this.playMenu = playMenu;
        loadDeck();

        InterfazServidor serverInterface = this;
        EsperaServidor waitingRoom = new EsperaServidor(playMenu, port, this);

        Thread waitingRoomThread = new Thread(){
            public void run(){
                // RECIBE LA INFORMACIÓN ACERCA DEL PERSONAJE OPONENTE
                // Creates the server and waits to receive the information about the opponent
                try{
                    server = new Servidor(ip, port);
                    // Carga los nombres
                    try {
                        exchangePlayersName();
                    } catch (Exception e) {
                        waitingRoom.dispose();
                        dispose();
                        return;
                    }
                    serverInterface.setVisible(true);
                    waitingRoom.dispose();
                    setBackground(0);
                    pointsBackground.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ opponentNumber +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
                } catch(IOException er){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    waitingRoom.dispose();
                    dispose();
                }
            }
        };
        waitingRoomThread.start();

        waitingRoom.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
        waitingRoom.setResizable(false);
        waitingRoom.setTitle("Esperando Conección");
        waitingRoom.setBounds(0,0,500,300);
        waitingRoom.setLocationRelativeTo(null);
        waitingRoom.setVisible(true);

        setLayout(null);
        setDefaultCloseOperation(3);

        // Inicializa la carta de movimiento
        // Initialize the moving card
        movingCard.setOpaque(false);
        movingCard.setContentAreaFilled(false);
        movingCard.setBorderPainted(false);

        setBackground(0);
        background.setBounds(0, 0, 500, 800);
        background.setVisible(true);
        add(background);

        cardOpponent1 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardOpponent1.setBounds(100, 70, 75, 100);
        cardOpponent1.setVisible(true);
        background.add(cardOpponent1);

        cardOpponent2 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardOpponent2.setBounds(200, 70, 75, 100);
        cardOpponent2.setVisible(true);
        background.add(cardOpponent2);

        cardOpponent3 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardOpponent3.setBounds(300, 70, 75, 100);
        cardOpponent3.setVisible(true);
        background.add(cardOpponent3);

        cardPlayer1 = new JButton();
        cardPlayer1.setBounds(10, 400, 155, 200);
        cardPlayer1.setVisible(true);
        cardPlayer1Enabled=false;
        cardPlayer1.setOpaque(false);
        cardPlayer1.setContentAreaFilled(false);
        cardPlayer1.setBorderPainted(false);
        background.add(cardPlayer1);
        cardPlayer1.addActionListener((ActionEvent e) -> {
            if(!cardPlayer1Enabled)
                return;
            effects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            effects.play();
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            try {
                throwCard(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la primer carta: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            setBackground(0);
        });

        cardPlayer2 = new JButton();
        cardPlayer2.setBounds(170, 400, 155, 200);
        cardPlayer2.setVisible(true);
        cardPlayer2Enabled=false;
        cardPlayer2.setBorderPainted(false);
        cardPlayer2.setOpaque(false);
        cardPlayer2.setContentAreaFilled(false);
        cardPlayer2.setBorderPainted(false);
        background.add(cardPlayer2);
        cardPlayer2.addActionListener((ActionEvent e) -> {
            if(!cardPlayer2Enabled)
                return;
            effects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            effects.play();
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            try {
                throwCard(1);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la segunda carta: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            setBackground(0);
        });

        cardPlayer3 = new JButton();
        cardPlayer3.setBounds(330, 400, 155, 200);
        cardPlayer3.setVisible(true);
        cardPlayer3Enabled=false;
        cardPlayer3.setBorderPainted(false);
        cardPlayer3.setOpaque(false);
        cardPlayer3.setContentAreaFilled(false);
        cardPlayer3.setBorderPainted(false);
        background.add(cardPlayer3);
        cardPlayer3.addActionListener((ActionEvent e) -> {
            if(!cardPlayer3Enabled)
                return;
            effects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            effects.play();
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            try {
                throwCard(2);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la tercer carta: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            setBackground(0);
        });

        JButton dealCards = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cartasMazo.png")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        dealCards.setBounds(365, 280, 80, 80);
        dealCards.setBorderPainted(false);
        dealCards.setOpaque(false);
        dealCards.setContentAreaFilled(false);
        dealCards.setBorderPainted(false);
        dealCards.setVisible(true);
        background.add(dealCards);
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

        cardThrownOpponent1 = new JLabel();
        cardThrownOpponent1.setBounds(130, 210, 70, 80);
        cardThrownOpponent1.setVisible(true);
        background.add(cardThrownOpponent1);

        cardThrownOpponent2 = new JLabel();
        cardThrownOpponent2.setBounds(210, 210, 70, 80);
        cardThrownOpponent2.setVisible(true);
        background.add(cardThrownOpponent2);

        cardThrownOpponent3 = new JLabel();
        cardThrownOpponent3.setBounds(290, 210, 70, 80);
        cardThrownOpponent3.setVisible(true);
        background.add(cardThrownOpponent3);

        cardThrownPlayer1 = new JLabel();
        cardThrownPlayer1.setBounds(130, 310, 70, 80);
        cardThrownPlayer1.setVisible(true);
        background.add(cardThrownPlayer1);

        cardThrownPlayer2 = new JLabel();
        cardThrownPlayer2.setBounds(210, 310, 70, 80);
        cardThrownPlayer2.setVisible(true);
        background.add(cardThrownPlayer2);

        cardThrownPlayer3 = new JLabel();
        cardThrownPlayer3.setBounds(290, 310, 70, 80);
        cardThrownPlayer3.setVisible(true);
        background.add(cardThrownPlayer3);

        statusBackground = new JLabel(new ImageIcon("src/truco_java/fondos/burbuja.png"));
        statusBackground.setBounds(50,170,400,45);
        statusBackground.setOpaque(false);
        statusBackground.setVisible(false);
        background.add(statusBackground);

        status = new JTextPane();
        status.setBounds(0,0,400,45);
        status.setFont(new Font("Serif", Font.ITALIC, 30));
        status.setEditable(false);
        status.setOpaque(false);
        status.setVisible(true);
        statusBackground.add(status);
        // Centers the text
        StyledDocument doc = status.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        envidoMenu = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        envidoMenu.setBounds(170, 660, 155, 60);
        envidoMenu.setVisible(true);
        envidoMenu.setEnabled(false);
        envidoMenu.setOpaque(false);
        envidoMenu.setContentAreaFilled(false);
        envidoMenu.setBorderPainted(false);
        background.add(envidoMenu);
        envidoMenu.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/boton.wav", 1);
            effects.play();
            if (envido.isVisible() == true) {
                envido.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
            } else {
                envido.setVisible(true);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
            }
        });

        irAlMazo = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/irAlMazoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        irAlMazo.setBounds(330, 660, 155, 60);
        irAlMazo.setVisible(true);
        irAlMazo.setEnabled(false);
        irAlMazo.setOpaque(false);
        irAlMazo.setContentAreaFilled(false);
        irAlMazo.setBorderPainted(false);
        background.add(irAlMazo);
        irAlMazo.addActionListener((ActionEvent e) -> {
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
            opponent.setPoints(opponent.getPoints()+points+calcularTrucoGanado(), this);
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

        envido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envido.setBounds(10, 595, 240, 50);
        envido.setVisible(false);
        envido.setOpaque(false);
        envido.setContentAreaFilled(false);
        envido.setBorderPainted(false);
        background.add(envido);
        envido.addActionListener((ActionEvent e) -> {
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
        });

        envidoEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envidoEnvido.setBounds(10, 595, 240, 50);
        envidoEnvido.setVisible(false);
        envidoEnvido.setOpaque(false);
        envidoEnvido.setContentAreaFilled(false);
        envidoEnvido.setBorderPainted(false);
        background.add(envidoEnvido);
        envidoEnvido.addActionListener((ActionEvent e) -> {
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
        });

        realEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/realEnvidoBoton.png")).getScaledInstance(117, 50, Image.SCALE_SMOOTH)));
        realEnvido.setBounds(252, 595, 117, 50);
        realEnvido.setVisible(false);
        realEnvido.setOpaque(false);
        realEnvido.setContentAreaFilled(false);
        realEnvido.setBorderPainted(false);
        background.add(realEnvido);
        realEnvido.addActionListener((ActionEvent e) -> {
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
        });

        faltaEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/faltaEnvidoBoton.png")).getScaledInstance(117, 50, Image.SCALE_SMOOTH)));
        faltaEnvido.setBounds(373, 595, 117, 50);
        faltaEnvido.setVisible(false);
        faltaEnvido.setOpaque(false);
        faltaEnvido.setContentAreaFilled(false);
        faltaEnvido.setBorderPainted(false);
        background.add(faltaEnvido);
        faltaEnvido.addActionListener((ActionEvent e) -> {
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
        });

        quieroEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/quieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        quieroEnvido.setBounds(10, 250, 110, 40);
        quieroEnvido.setVisible(false);
        quieroEnvido.setOpaque(false);
        quieroEnvido.setContentAreaFilled(false);
        quieroEnvido.setBorderPainted(false);
        background.add(quieroEnvido);
        quieroEnvido.addActionListener((ActionEvent e) -> {
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
        });

        noQuieroEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/noQuieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        noQuieroEnvido.setBounds(10, 300, 110, 40);
        noQuieroEnvido.setVisible(false);
        noQuieroEnvido.setOpaque(false);
        noQuieroEnvido.setContentAreaFilled(false);
        noQuieroEnvido.setBorderPainted(false);
        background.add(noQuieroEnvido);
        noQuieroEnvido.addActionListener((ActionEvent e) -> {
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
        });

        truco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setBounds(10, 660, 155, 60);
        truco.setVisible(true);
        truco.setEnabled(false);
        truco.setOpaque(false);
        truco.setContentAreaFilled(false);
        truco.setBorderPainted(false);
        background.add(truco);
        truco.addActionListener((ActionEvent e) -> {
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
        });

        quieroTruco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/quieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        quieroTruco.setBounds(10, 250, 110, 40);
        quieroTruco.setVisible(false);
        quieroTruco.setOpaque(false);
        quieroTruco.setContentAreaFilled(false);
        quieroTruco.setBorderPainted(false);
        background.add(quieroTruco);
        quieroTruco.addActionListener((ActionEvent e) -> {
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
        });


        noQuieroTruco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/noQuieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        noQuieroTruco.setBounds(10, 300, 110, 40);
        noQuieroTruco.setVisible(false);
        noQuieroTruco.setOpaque(false);
        noQuieroTruco.setContentAreaFilled(false);
        noQuieroTruco.setBorderPainted(false);
        background.add(noQuieroTruco);
        noQuieroTruco.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/boton.wav", 1);
            effects.play();
            opponent.setPoints(opponent.getPoints() + calcularTrucoPerdido(), this);
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
        });

        pointsBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ opponentNumber +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
        pointsBackground.setBounds(390, 10, 100, 150);
        pointsBackground.setVisible(true);
        background.add(pointsBackground);

        pointsPlayer.setBounds(0, 65, 50, 85);
        pointsPlayer.setVisible(true);
        pointsBackground.add(pointsPlayer);

        pointsAi.setBounds(50, 65, 50, 85);
        pointsAi.setVisible(true);
        pointsBackground.add(pointsAi);

        JButton back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setBounds(10, 10, 50, 50);
        back.setVisible(true);
        background.add(back);
        back.addActionListener((ActionEvent e) -> {
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
        });

        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
        connectionBackground.setBounds(0,730,500,50);
        connectionBackground.setOpaque(false);
        connectionBackground.setVisible(true);
        background.add(connectionBackground);
    }

    public void updatePoints() throws IOException {
        if(finishedGame)
            return;
        int playerPoints = player.getPoints();
        int opponentPoints = opponent.getPoints();

        if(playerPoints > 15)
            playerPoints = 15;
        if(opponentPoints > 15)
            opponentPoints = 15;

        pointsPlayer.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + playerPoints + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));
        pointsAi.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + opponentPoints + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));


        if(playerPoints==15){
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
        if(opponentPoints==15){
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
    }

    private void loadDeck() {
        deck.add(new Carta(1, "espada"));
        deck.add(new Carta(2, "espada"));
        deck.add(new Carta(3, "espada"));
        deck.add(new Carta(4, "espada"));
        deck.add(new Carta(5, "espada"));
        deck.add(new Carta(6, "espada"));
        deck.add(new Carta(7, "espada"));
        deck.add(new Carta(10, "espada"));
        deck.add(new Carta(11, "espada"));
        deck.add(new Carta(12, "espada"));
        deck.add(new Carta(1, "basto"));
        deck.add(new Carta(2, "basto"));
        deck.add(new Carta(3, "basto"));
        deck.add(new Carta(4, "basto"));
        deck.add(new Carta(5, "basto"));
        deck.add(new Carta(6, "basto"));
        deck.add(new Carta(7, "basto"));
        deck.add(new Carta(10, "basto"));
        deck.add(new Carta(11, "basto"));
        deck.add(new Carta(12, "basto"));
        deck.add(new Carta(1, "oro"));
        deck.add(new Carta(2, "oro"));
        deck.add(new Carta(3, "oro"));
        deck.add(new Carta(4, "oro"));
        deck.add(new Carta(5, "oro"));
        deck.add(new Carta(6, "oro"));
        deck.add(new Carta(7, "oro"));
        deck.add(new Carta(10, "oro"));
        deck.add(new Carta(11, "oro"));
        deck.add(new Carta(12, "oro"));
        deck.add(new Carta(1, "copa"));
        deck.add(new Carta(2, "copa"));
        deck.add(new Carta(3, "copa"));
        deck.add(new Carta(4, "copa"));
        deck.add(new Carta(5, "copa"));
        deck.add(new Carta(6, "copa"));
        deck.add(new Carta(7, "copa"));
        deck.add(new Carta(10, "copa"));
        deck.add(new Carta(11, "copa"));
        deck.add(new Carta(12, "copa"));
    }

    private void mixDeck() {
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            ArrayList<Carta> tempDeck = new ArrayList<>();
            for (int x = 0; x < deck.size() + tempDeck.size(); x++) {
                int mixingPos = random.nextInt(deck.size());
                tempDeck.add(deck.get(mixingPos));
                deck.remove(mixingPos);
            }
            deck.clear();
            deck.addAll(tempDeck);
        }
    }

    private void drawCards() throws IOException {
        // TODO: CHECK IF I DON'T NEED THIS, BECAUSE NOW I PUT IT WHEN THE CARDS ARE INITIALIZED
        // cardAi1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        // cardAi2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        // cardAi3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));

        switch (opponent.getCards().size()) {
            case 0:
                cardOpponent1.setVisible(false);
                cardOpponent2.setVisible(false);
                cardOpponent3.setVisible(false);
                break;
            case 1:
                cardOpponent1.setVisible(true);
                cardOpponent2.setVisible(false);
                cardOpponent3.setVisible(false);
                break;
            case 2:
                cardOpponent1.setVisible(true);
                cardOpponent2.setVisible(true);
                cardOpponent3.setVisible(false);
                break;
            case 3:
                cardOpponent1.setVisible(true);
                cardOpponent2.setVisible(true);
                cardOpponent3.setVisible(true);
                break;
        }

        ArrayList<JButton> cards = new ArrayList<>();
        cards.add(cardPlayer1);
        cards.add(cardPlayer2);
        cards.add(cardPlayer3);

        for(int i=0;i<3;i++){
            if(player.getPosCards()[i]<0){
                cards.get(i).setVisible(false);
                cards.get(i).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            } else {
                cards.get(i).setVisible(true);
                cards.get(i).setIcon(new ImageIcon(ImageIO.read(new File(player.getCards().get(player.getPosCards()[i]).linkCard())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            }
        }

        switch (opponent.getPlayedCards().size()) {
            case 3:
                cardThrownOpponent1.setIcon(new ImageIcon(ImageIO.read(new File(opponent.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownOpponent2.setIcon(new ImageIcon(ImageIO.read(new File(opponent.getPlayedCards().get(1).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownOpponent3.setIcon(new ImageIcon(ImageIO.read(new File(opponent.getPlayedCards().get(2).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                cardThrownOpponent1.setIcon(new ImageIcon(ImageIO.read(new File(opponent.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownOpponent2.setIcon(new ImageIcon(ImageIO.read(new File(opponent.getPlayedCards().get(1).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownOpponent3.setIcon(null);
                break;
            case 1:
                cardThrownOpponent1.setIcon(new ImageIcon(ImageIO.read(new File(opponent.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownOpponent2.setIcon(null);
                cardThrownOpponent3.setIcon(null);
                break;
            case 0:
                cardThrownOpponent1.setIcon(null);
                cardThrownOpponent2.setIcon(null);
                cardThrownOpponent3.setIcon(null);
                break;
        }

        switch (player.getPlayedCards().size()) {
            case 3:
                cardThrownPlayer1.setIcon(new ImageIcon(ImageIO.read(new File(player.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownPlayer2.setIcon(new ImageIcon(ImageIO.read(new File(player.getPlayedCards().get(1).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownPlayer3.setIcon(new ImageIcon(ImageIO.read(new File(player.getPlayedCards().get(2).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                cardThrownPlayer1.setIcon(new ImageIcon(ImageIO.read(new File(player.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownPlayer2.setIcon(new ImageIcon(ImageIO.read(new File(player.getPlayedCards().get(1).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownPlayer3.setIcon(null);
                break;
            case 1:
                cardThrownPlayer1.setIcon(new ImageIcon(ImageIO.read(new File(player.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownPlayer2.setIcon(null);
                cardThrownPlayer3.setIcon(null);
                break;
            case 0:
                cardThrownPlayer1.setIcon(null);
                cardThrownPlayer2.setIcon(null);
                cardThrownPlayer3.setIcon(null);
                break;
        }
    }

    private void drawButtons() throws IOException {
        switch (trucoLevel) {
            case 0:
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                break;
            case 1:
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/retrucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                break;
            case 2:
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/valeCuatroBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                break;
        }
    }

    private void throwCard(int pos) throws IOException {
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

    private void anotherRound() throws IOException {
        if(!finishedGame && Truco_Java.musicCheckBox.isSelected() && !menu.fastModeCheckBox.isSelected()) {
            effects.setFile("src/truco_java/musica/otraPartida.wav", 1);
            effects.play();
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ex) {
                Logger.getLogger(InterfazServidor.class.getName()).log(Level.SEVERE, null, ex);
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
        ArrayList<Carta> cards1 = new ArrayList<>();
        cards1.add(deck.get(0));
        cards1.add(deck.get(2));
        cards1.add(deck.get(4));
        ArrayList<Carta> cards2 = new ArrayList<>();
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

    private void updatesTurn() throws IOException {
        if(finishedGame)
            return;
        if(checksWinnerOfGame()==1) {
            server.sendMessage("imprimir Termino la Partida. Ha ganado " + playerName);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
            ThreadOptionPane("Termino la Partida. Ganaste!");

            player.setPoints(player.getPoints() + calcularTrucoGanado(), this);
            anotherRound();
            updatesTurn();
            return;
        }
        if(checksWinnerOfGame()==2) {
            server.sendMessage("imprimir Termino la Partida. Ganaste!");
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
            ThreadOptionPane("Termino la Partida. Ha ganado " + opponentName + ".");

            opponent.setPoints(opponent.getPoints() + calcularTrucoGanado(), this);
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

    private int countPointsWonEnvido(int looserPoints) {
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

    private int countPointsLoserEnvido() {
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

    private void printsEnvidoMessage(int envido, boolean isCalledFromTimer){
        String text = " ";
        statusBackground.setVisible(true);
        status.setFont(new Font("Serif", Font.ITALIC, 30));
        status.setVisible(true);

        if(!isCalledFromTimer && envido!=0) {
            opponentVoice.setFile("src/truco_java/cantos/envido/" + opponentNumber + envido + ".wav", 1);
            opponentVoice.play();
        }

        switch(envido){
            case -1:
                text = "No quiero!";
                if(opponentNumber==5) text = "No tea? so No quiero!";
                break;
            case 0:
                statusBackground.setVisible(false);
                status.setText("");
                setBackground(0);
                return;
            case 1:
                text = "Envido!";
                if(opponentNumber==5) text = "Why not? Envido!";
                break;
            case 2:
                text = "Envido!";
                if(opponentNumber==5) {
                    text = "What about another Envido?!";
                    status.setFont(new Font("Serif", Font.ITALIC, 25));
                }
                break;
            case 3:
                text = "Real Envido!";
                if(opponentNumber==5) text = "I say Real Envido!";
                break;
            case 4:
                text = "Falta Envido!";
                if(opponentNumber==5) text = "Falta Envido my mate!";
                break;
            case 5:
                text = "Quiero!";
                if(opponentNumber==5) text = "Easy peasy. Quiero!";
                break;
        }

        status.setText(text);
        setBackground(1);

        // I have to create a final variable for the timer, otherwise it will complain
        final String timerText = text;
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    // If no other method changed the text of the panel (for example: 'retrucar' or 'envido')
                    if(status.getText().equals(timerText)){
                        if(quieroEnvido.isVisible())
                            printsEnvidoMessage(envido, true); // Prints again
                        else printsEnvidoMessage(0, true); // Clears
                    }
                }
            },
            2000
            );
    }

    private int checksWinnerOfGame(){
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

    private void printsTrucoMessage(int trucoMessage, boolean isCalledFromTimer) throws IOException{
        setBackground(1);
        statusBackground.setVisible(true);

        if(!isCalledFromTimer && trucoMessage!=0) {
            opponentVoice.setFile("src/truco_java/cantos/truco/" + opponentNumber + trucoMessage + ".wav", 1);
            opponentVoice.play();
        }

        // TODO: Put every `status.setText("xxxxxxx")` in a variable `text`, that after the switch calls the status.setText("xxxxxxx"). And update the `timerText` initialization
        switch(trucoMessage){
            case -1:
                status.setText("No quiero!");
                if(opponentNumber==5) status.setText("This’s a rip-off. No quiero");
                break;
            case 0:
                statusBackground.setVisible(false);
                status.setText("");
                setBackground(0);
                return;
            case 1:
                status.setText("Truco!");
                if(opponentNumber==5) status.setText("Come on, Truco!");
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/retrucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                truco.setEnabled(true);
                break;
            case 2:
                status.setText("Re truco!");
                if(opponentNumber==5) status.setText("Re truco if you're brave!");
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/valeCuatroBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                truco.setEnabled(true);
                break;
            case 3:
                status.setText("Quiero vale 4!");
                if(opponentNumber==5) status.setText("Really? Quiero vale cuatro");
                truco.setEnabled(false);
                break;
            case 4:
                status.setText("Quiero!");
                if(opponentNumber==5) status.setText("Easy peasy. Quiero!");
                truco.setEnabled(false);
                break;
        }

        finishedEnvido = true;
        final String timerText = status.getText();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if(status.getText().equals(timerText)){
                            if(quieroTruco.isVisible()){
                                try {
                                    printsTrucoMessage(trucoMessage, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + opponentName + " en el truco: " + ex.getMessage());
                                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    effects.play();
                                }
                            }
                            else {
                                try {
                                    printsTrucoMessage(0, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + opponentName + " en el truco: " + ex.getMessage());
                                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    effects.play();
                                }
                            }
                        }
                    }
                },
                2000
        );
    }

    private int calcularTrucoGanado() {
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

    private int calcularTrucoPerdido() {
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

    private void setBackground(int status){
        char statusChar = status==0 ? 'a' : 'b';
        // a ==> Normal Background
        // b ==> Expression Background

        String image = "src/truco_java/fondos/bg" + opponentNumber + statusChar + ".png";
        background.setIcon(new ImageIcon(image));
    }

    // TODO: Replace every hard-coded value into its corresponding property
    private void moveCardOpponent(int origin, int destination) throws IOException{
        final int originX, destinationX;
        final String file = opponent.getPlayedCards().get(opponent.getPlayedCards().size()-1).linkCard();

        // Depending on what card is, it hides it and puts a temporal one in replace
        switch(origin){
            case 0:
                cardOpponent1.setVisible(false);
                originX=100;
                break;
            case 1:
                cardOpponent2.setVisible(false);
                originX=180;
                break;
            case 2:
                cardOpponent3.setVisible(false);
                originX=300;
                break;
            default:
                originX=0;
                break;
        }
        switch(destination){
            case 0:
                destinationX=130;
                break;
            case 1:
                destinationX=220;
                break;
            case 2:
                destinationX=300;
                break;
            default:
                destinationX=0;
                break;
        }

        if(menu.fastModeCheckBox.isSelected()){
            try {
                drawCards();
                updatesTurn();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
            return;
        }

        movingCard.setBounds(originX, 70, 75, 100);
        movingCard.setVisible(true);
        background.add(movingCard);

        int movementPercentage=40;
        final int moveX=(destinationX-originX)/movementPercentage, moveY=(210-70)/movementPercentage;
        final int sizeX = (movingCard.getWidth()-70)/movementPercentage;
        final int sizeY = (movingCard.getHeight()-80)/movementPercentage;

        Thread thread = new Thread(){
            public void run(){
                moveCardTimer(moveX, moveY, originX, 70, destinationX, 210, sizeX, sizeY, file);
            }
        };
        thread.start();
    }

    private void moveCardPlayer(int origin, String file, int destination) throws IOException{
        final int originX, destinationX;

        // Depending on what card is, it hides it and puts a temporal one in replace
        switch(origin){
            case 0:
                cardPlayer1.setVisible(false);
                originX=10;
                break;
            case 1:
                cardPlayer2.setVisible(false);
                originX=170;
                break;
            case 2:
                cardPlayer3.setVisible(false);
                originX=330;
                break;
            default:
                originX=0;
                break;
        }
        switch(destination){
            case 0:
                destinationX=130;
                break;
            case 1:
                destinationX=210;
                break;
            case 2:
                destinationX=290;
                break;
            default:
                destinationX=0;
                break;
        }

        movingCard.setBounds(originX, 400, 155, 200);
        movingCard.setVisible(true);
        background.add(movingCard);

        int movementPorcentage=40;
        final int moveX=(destinationX-originX)/movementPorcentage, moveY=(310-400)/movementPorcentage;
        final int sizeX = (movingCard.getWidth()-70)/movementPorcentage;
        final int sizeY = (movingCard.getHeight()-80)/movementPorcentage;

        Thread thread = new Thread(){
            public void run(){
                moveCardTimer(moveX, moveY, originX, 400, destinationX, 310, sizeX, sizeY, file);
            }
        };
        thread.start();
    }

  public void moveCardTimer (int moveX, int moveY, int originX, int originY, int destinationX, int destinationY, int width, int height, String file) {
      new java.util.Timer().schedule(
            new java.util.TimerTask() {
              @Override
              public void run() {
                  movingCard.setBounds(originX+moveX,originY+moveY, movingCard.getWidth()-width, movingCard.getHeight()-height);
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
      if(originX-destinationX>5 || originX-destinationX<-5){
          moveCardTimer(moveX,moveY, originX+moveX, originY+moveY, destinationX, destinationY, width, height, file);
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

    public void decodeMessage (String message) {
        message=message.trim();
        // TODO: INVESTIGATE IF THE TRIM INSIDE THE IF CAN BE DELETED
        if(message.trim().isEmpty())
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
                    moveCardOpponent(opponent.getCards().size(), opponent.getPlayedCards().size()-1);
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
                        player.setPoints(player.getPoints() + calcularTrucoPerdido(), this);
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
                player.setPoints(player.getPoints() + points + calcularTrucoGanado(), this);
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
            server.updateInfo(player.cards.size(), player.getPlayedCards(), opponent.getCards(), opponent.getPosCards(), opponent.getPlayedCards(),trucoLevel, finishedEnvido, enabledToRetrucar, opponentTurn, player.getPoints(), opponent.getPoints());
        } catch (IOException e) {
            for(int i=0;i<30;i++){
                try {
                    Thread.sleep(500);
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    server.updateInfo(player.cards.size(), player.getCards(), opponent.getCards(), opponent.getPosCards(), opponent.getPlayedCards(), trucoLevel, finishedEnvido, enabledToRetrucar, opponentTurn, player.getPoints(), opponent.getPoints());
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
