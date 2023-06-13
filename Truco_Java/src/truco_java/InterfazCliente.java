package truco_java;

import java.util.Scanner;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
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

public class InterfazCliente extends JFrame {
    Cliente client;

    // Swing
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
    private JLabel pointsAi = new JLabel(), puntajeJugador = new JLabel();
    private JTextPane status;
    private JButton movingCard = new JButton();
    private boolean PC1Enabled=false, PC2Enabled=false, PC3Enabled=false;
    private final Music opponentVoice = new Music();
    private static final Music efects = new Music();
    private JLabel connectionBackground = new JLabel(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
    private boolean finishedGame = false;
    JLabel pointsBackground = new JLabel();

    // Players
    private Persona player = new Persona(null, true);
    private int opponentNumber; // Represents the player that was selected personaje que fue generado
    private String opponentName ="la PC";
    private Persona opponent = new Persona(null, false);

    // Information of the game
    private ArrayList<Integer> envidosDeclared = new ArrayList<>();
    private int opponentCardsCount;
    private int trucoLevel;
    private boolean finishedEnvido;
    private int enabledToRetrucar; // 2--> Player; 1--> opponent

    public InterfazCliente(Truco_Java menu, String ip, int port, MenuJugar playMenu) throws IOException {
        client = new Cliente(ip,port);
        this.menu = menu;
        this.playMenu = playMenu;

        Thread waitingForName = new Thread(){
            public void run(){
                try{
                    decodeMessage(client.receiveMessage());
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el nombre del Jugador: " + e.getMessage());
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
                }
                setBackground(0);
                try{
                    decodeMessage(client.receiveMessage());
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al recibir el mensaje: " + e.getMessage());
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
                }
            }
        };
        waitingForName.start();

        setLayout(null);
        setDefaultCloseOperation(3);

        movingCard.setOpaque(false);
        movingCard.setContentAreaFilled(false);
        movingCard.setBorderPainted(false);

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
        PC1Enabled=false;
        cardPlayer1.setOpaque(false);
        cardPlayer1.setContentAreaFilled(false);
        cardPlayer1.setBorderPainted(false);
        background.add(cardPlayer1);
        cardPlayer1.addActionListener((ActionEvent e) -> {
            if(!PC1Enabled)
                return;
            efects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efects.play();
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            try {
                throwCard(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la primer carta: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
            }
            setBackground(0);
        });

        cardPlayer2 = new JButton();
        cardPlayer2.setBounds(170, 400, 155, 200);
        cardPlayer2.setVisible(true);
        PC2Enabled=false;
        cardPlayer2.setBorderPainted(false);
        cardPlayer2.setOpaque(false);
        cardPlayer2.setContentAreaFilled(false);
        cardPlayer2.setBorderPainted(false);
        background.add(cardPlayer2);
        cardPlayer2.addActionListener((ActionEvent e) -> {
            if(!PC2Enabled)
                return;
            efects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efects.play();
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            try {
                throwCard(1);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la segunda carta: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
            }
            setBackground(0);
        });

        cardPlayer3 = new JButton();
        cardPlayer3.setBounds(330, 400, 155, 200);
        cardPlayer3.setVisible(true);
        PC3Enabled=false;
        cardPlayer3.setBorderPainted(false);
        cardPlayer3.setOpaque(false);
        cardPlayer3.setContentAreaFilled(false);
        cardPlayer3.setBorderPainted(false);
        background.add(cardPlayer3);
        cardPlayer3.addActionListener((ActionEvent e) -> {
            if(!PC3Enabled)
                return;
            efects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efects.play();
            envidoMenu.setEnabled(false);
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            try {
                throwCard(2);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la tercer carta: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
        dealCards.setEnabled(false);
        background.add(dealCards);

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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
            Thread thread = new Thread(){
                public void run(){
                    try{
                        decodeMessage(client.sendIrAlMazo());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }
            };
            thread.start();
            JOptionPane.showMessageDialog(null, "Te has ido al mazo.");
            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efects.play();
        });

        envido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envido.setBounds(10, 595, 240, 50);
        envido.setVisible(false);
        envido.setOpaque(false);
        envido.setContentAreaFilled(false);
        envido.setBorderPainted(false);
        background.add(envido);
        envido.addActionListener((ActionEvent e) -> {
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
            envidosDeclared.add(1);
            truco.setEnabled(false);
            try {
                trucoLevel=0;
                drawButtons();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
            enabledToRetrucar=1;

            trucoLevel=0;
            enabledToRetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                        decodeMessage(client.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
            envidosDeclared.add(2);
            truco.setEnabled(false);
            try {
                trucoLevel=0;
                drawButtons();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
            enabledToRetrucar=1;

            trucoLevel=0;
            enabledToRetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                        decodeMessage(client.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
            envidosDeclared.add(3);
            truco.setEnabled(false);
            try {
                trucoLevel=0;
                drawButtons();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
            enabledToRetrucar=1;

            trucoLevel=0;
            enabledToRetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                        decodeMessage(client.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
            envidosDeclared.add(4);
            truco.setEnabled(false);
            try {
                trucoLevel=0;
                drawButtons();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
            enabledToRetrucar=1;

            trucoLevel=0;
            enabledToRetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        client.sendEnvido(envidosDeclared, trucoLevel, enabledToRetrucar);
                        decodeMessage(client.receiveMessage());
                    } catch(IOException er){
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
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
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }
            };
            thread.start();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
            envidosDeclared.add(-1);
            truco.setEnabled(false);
            try {
                trucoLevel=0;
                drawButtons();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }
            };
            thread.start();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
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
                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
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
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                        }
                    }
                };
                thread.start();
                try {
                    printsTrucoMessage(0, false);
                } catch (Exception ex) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + ex.getMessage());
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
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
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }
            };
            thread.start();
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
            efects.setFile("src/truco_java/musica/boton.wav", 1);
            efects.play();
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
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }
            };
            thread.start();
            setBackground(0);
        });

        pointsBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ opponentNumber +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
        pointsBackground.setBounds(390, 10, 100, 150);
        pointsBackground.setVisible(true);
        background.add(pointsBackground);

        puntajeJugador.setBounds(0, 65, 50, 85);
        puntajeJugador.setVisible(true);
        pointsBackground.add(puntajeJugador);

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
            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efects.play();

            int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + opponentName + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efects.play();
            if(dialogResult == JOptionPane.YES_OPTION){
                try {
                    client.sendMessage("retira");
                } catch (Exception ex) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
                }
                playMenu.setVisible(true);
                dispose();
            }
        });

        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        connectionBackground.setBounds(0,730,500,50);
        connectionBackground.setOpaque(false);
        connectionBackground.setVisible(true);
        background.add(connectionBackground);
    }

    public void updatePoints() throws IOException {
        if(finishedGame)
            return;
        int playerPoints = player.getPoints();
        int aiPoints = opponent.getPoints();
        if(playerPoints > 15)
            playerPoints = 15;
        if(aiPoints > 15)
            aiPoints = 15;

        puntajeJugador.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + playerPoints + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));
        pointsAi.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + aiPoints + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));

    }

    private void drawCards() throws IOException {
        // TODO: CHECK IF I DON'T NEED THIS, BECAUSE NOW I PUT IT WHEN THE CARDS ARE INITIALIZED
        // cardAi1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        // cardAi2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        // cardAi3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));

        switch (opponentCardsCount) {
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

        ArrayList<JButton> manos = new ArrayList<>();
        manos.add(cardPlayer1);
        manos.add(cardPlayer2);
        manos.add(cardPlayer3);

        for(int i=0;i<3;i++){
            if(player.getPosCards()[i]<0){
                manos.get(i).setVisible(false);
                manos.get(i).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            } else {
                manos.get(i).setVisible(true);
                manos.get(i).setIcon(new ImageIcon(ImageIO.read(new File(player.getCards().get(player.getPosCards()[i]).linkCard())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
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
        PC1Enabled=false;
        PC2Enabled=false;
        PC3Enabled=false;

        if(!menu.fastModeCheckBox.isSelected()) moveCardPlayer(pos, player.getCards().get(player.getPosCards()[pos]).linkCard(), player.getPlayedCards().size());

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
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            client.throwCard(pos);
                            decodeMessage(client.receiveMessage());
                        } catch(IOException er){
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efects.play();
                        }
                    }
                };
                thread.start();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
            }
        }
    }

    private void printsEnvidoMessage(int envido, boolean isCalledFromTimer){
        String text = " ";
        statusBackground.setVisible(true);
        status.setFont(new Font("Serif", Font.ITALIC, 30));
        status.setVisible(true);

        if(!isCalledFromTimer && envido!=0) {
            if(envido==5) opponentVoice.setFile("src/truco_java/cantos/truco/" + opponentNumber + "4.wav", 1);
            else opponentVoice.setFile("src/truco_java/cantos/envido/" + opponentNumber + envido + ".wav", 1);
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
                                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    efects.play();
                                }
                            }
                            else {
                                try {
                                    printsTrucoMessage(0, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + opponentName + " en el truco: " + ex.getMessage());
                                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    efects.play();
                                }
                            }
                        }
                    }
                },
                2000
        );
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
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efects.play();
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
                moveCardTimer(moveX, moveY, originX, 70, destinationX, 210, sizeX, sizeY, file, -1);
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

        int porcentajeMov=40;
        final int moveX=(destinationX-originX)/porcentajeMov, moveY=(310-400)/porcentajeMov;
        final int sizeX = (movingCard.getWidth()-70)/porcentajeMov;
        final int sizeY = (movingCard.getHeight()-80)/porcentajeMov;

        Thread thread = new Thread(){
            public void run(){
                moveCardTimer(moveX, moveY, originX, 400, destinationX, 310, sizeX, sizeY, file, origin);
            }
        };
        thread.start();
    }

  public void moveCardTimer (int moveX, int moveY, int originX, int originY, int destinationX, int destinationY, int width, int height, String file, int pos) {
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
          moveCardTimer(moveX,moveY, originX+moveX, originY+moveY, destinationX, destinationY, width, height, file, pos);
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
          if(pos!=-1) {
              Thread thread = new Thread(){
                  public void run(){
                      try{
                          client.throwCard(pos);
                          decodeMessage(client.receiveMessage());
                      } catch(IOException er){
                          connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                          efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          efects.play();
                      }
                  }
              };
              thread.start();
          }
      } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar las cartas: " + ex.getMessage());
          efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
          efects.play();
      }
  }

  public void decodeMessage (String message) {
      if(message==""){
          try {
              decodeMessage(client.receiveMessage());
          } catch (Exception e) {
              connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
              JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + e.getMessage());
              efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
              efects.play();
          }
          return;
      }
      Scanner scanf = new Scanner(message.trim());
      String cat="";
      if(scanf.hasNext()) cat = scanf.next();
      else {
          connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
          JOptionPane.showMessageDialog(null, "Ha sucedido un error en la comunicación. Mensaje corrupto");
          efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
          efects.play();
          Thread thread = new Thread(){
              public void run(){
                  try{
                      decodeMessage(client.receiveMessage());
                  } catch(IOException er){
                      connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                      JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                      efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                      efects.play();
                  }
              }
          };
          thread.start();
          return;
      }

        switch(cat){
            case "update":
                int newCountCardsOpponent=Integer.parseInt(scanf.next());
                boolean moveCardOpponent=false;
                if(newCountCardsOpponent < opponentCardsCount)
                    moveCardOpponent = true;
                opponentCardsCount=newCountCardsOpponent;

                ArrayList<Carta> playedCardsOpponent = new ArrayList<>();
                for(int i=0;i<3-opponentCardsCount;i++)
                    playedCardsOpponent.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                opponent.setPlayedCards(playedCardsOpponent);

                ArrayList<Carta> cards = new ArrayList<>();
                int sizeCards = Integer.parseInt(scanf.next());
                for(int i=0;i<sizeCards;i++)
                    cards.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                player.setCards(cards);

                int[] positionCards = new int[3];
                for(int i=0;i<3;i++)
                    positionCards[i]=Integer.parseInt(scanf.next());
                player.setPosCards(positionCards);

                ArrayList<Carta> playedCards = new ArrayList<>();
                for(int i=0;i<3-sizeCards;i++)
                    playedCards.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                player.setPlayedCards(playedCards);

                trucoLevel=Integer.parseInt(scanf.next());
                finishedEnvido=Boolean.parseBoolean(scanf.next());
                enabledToRetrucar=Integer.parseInt(scanf.next());

                if(moveCardOpponent){
                    try {
                        moveCardOpponent(newCountCardsOpponent, 2-newCountCardsOpponent);
                    } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }

                // It's player's turn
                if(Boolean.parseBoolean(scanf.next())){
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
                    PC1Enabled=true;
                    PC2Enabled=true;
                    PC3Enabled=true;

                    try {
                        drawButtons();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }

                    if((enabledToRetrucar==2 || enabledToRetrucar==0) && trucoLevel!=3)
                        truco.setEnabled(true);
                    else
                        truco.setEnabled(false);
                    // If the last card is 4, disable 'truco'
                    if(opponent.getPlayedCards().size() == 3){
                        if(opponent.getPlayedCards().get(2).rankingCard()==0) {
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
                    PC1Enabled=false;
                    PC2Enabled=false;
                    PC3Enabled=false;
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
                                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                efects.play();
                            }
                        }
                    };
                    thread.start();
                }

                if(!moveCardOpponent){
                    try {
                        drawCards();
                    } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                }

                opponent.setPoints(Integer.parseInt(scanf.next()), this);
                player.setPoints(Integer.parseInt(scanf.next()), this);

                try {
                    updatePoints();
                } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                }
                break;
            case "envido":
                int level = Integer.parseInt(scanf.next());
                trucoLevel = Integer.parseInt(scanf.next());
                enabledToRetrucar = Integer.parseInt(scanf.next());
                printsEnvidoMessage(level, false);
                setBackground(1);

                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
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
                                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                efects.play();
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
            case "imprimir":
                String textToPrint="";
                while(scanf.hasNext()){
                    String temp = scanf.next() + " ";
                    if(!temp.equals("ç "))
                        textToPrint+=temp;
                    else break;
                }
                final String imprime = textToPrint;
                Thread thread2 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, imprime);
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
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
                            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efects.play();
                        }
                    }
                };
                thread.start();
                break;
            case "kill":
                playMenu.setVisible(true);
                dispose();
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
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
                }
                if(newTrucoLevel!=4 && newTrucoLevel!=-1){
                    quieroTruco.setVisible(true);
                    noQuieroTruco.setVisible(true);
                    trucoLevel=newTrucoLevel;
                    try {
                        drawButtons();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + e.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                    }
                } else {
                    truco.setEnabled(false);
                    quieroTruco.setVisible(false);
                    noQuieroTruco.setVisible(false);
                    if(newTrucoLevel==-1){
                        Thread thread3 = new Thread(){
                            public void run(){
                                JOptionPane.showMessageDialog(null, "" + opponentName + " ha rechazado el Truco. Repartiendo...");
                                efects.setFile("src/truco_java/musica/boton.wav", 1);
                                efects.play();
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
                                efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                efects.play();
                            }
                        }
                    };
                    thread1.start();
                }
                break;
            case "puntaje":
                opponent.setPoints(Integer.parseInt(scanf.next()), this);
                player.setPoints(Integer.parseInt(scanf.next()), this);
                try {
                    updatePoints();
                } catch (Exception e) {
                        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efects.play();
                }
                Thread thread3 = new Thread(){
                    public void run(){
                        try{
                            decodeMessage(client.receiveMessage());
                        } catch(IOException er){
                            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efects.play();
                        }
                    }
                };
                thread3.start();
                break;
            case "retira":
                opponent.setPoints(15, this);
                JOptionPane.showMessageDialog(null, "El oponente " + opponentName + " se ha retirado. Has ganado!");
                playMenu.setVisible(true);
                dispose();
                break;
            case "persona":
                opponentNumber = Integer.parseInt(scanf.next());
                opponentName="";
                while(scanf.hasNext()){
                    String nombreTemp = scanf.next();
                    if(!nombreTemp.equals("ç")) opponentName += nombreTemp;
                    if(scanf.hasNext()) opponentName+=" ";
                }
                String nombre = "";
                if(Truco_Java.userIndex!=-1)
                    nombre = Truco_Java.userList.get(Truco_Java.userIndex).getName();

                Thread thread1 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, opponentName + " ha entrado en la sala. Esperando a que el oponente reparta...");
                        efects.setFile("src/truco_java/musica/boton.wav", 1);
                        efects.play();
                    }
                };
                thread1.start();
                try {
                    client.sendPerson(MenuJugar.playerNumber+1, nombre);
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + e.getMessage());
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
                }
                try {
                    pointsBackground.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ opponentNumber +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
                } catch (Exception e) {
                    connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                    efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efects.play();
                }
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
