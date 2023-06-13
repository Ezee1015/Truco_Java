package truco_java;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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

public class SinglePlayer extends JFrame {

    // Swing
    private JLabel background = new JLabel();
    private JLabel cardAi1, cardAi2, cardAi3;
    private JLabel cardThrownAi1, cardThrownAi2, cardThrownAi3;
    private JButton cardPlayer1, cardPlayer2, cardPlayer3;
    private JLabel cardThrownPlayer1, cardThrownPlayer2, cardThrownPlayer3;
    private JButton truco, envidoMenu, irAlMazo, envido, envidoEnvido, realEnvido, faltaEnvido;
    private JButton noQuieroEnvido, quieroEnvido, noQuieroTruco, quieroTruco;
    private JTextPane status;
    private JLabel statusBackground;
    private JLabel pointsAi = new JLabel(), pointsPlayer = new JLabel();
    private JButton movingCard = new JButton();

    // Players
    private Ai ai = new Ai(null, false);
    private Person player = new Person(null, true);

    // Information of the game
    private Truco_Java menu;
    private ArrayList<Card> deck = new ArrayList<>();
    private boolean cardPlayer1Enabled=false, cardPlayer2Enabled=false, cardPlayer3Enabled=false;
    private boolean finishedGame = false;
    private final int aiNumber = PlayMenu.playerNumber+1; // Represents the character that was selected
    private String aiName ="the PC";
    private final Music aiVoice = new Music();
    private static final Music effects = new Music();
    private int enabledToRetrucar = 0; // 1--> Player; 2--> AI
    private int trucoLevel = 0;
    private ArrayList<Integer> envidosDeclared;
    private boolean finishedEnvido = false;

    public SinglePlayer(Truco_Java menu) throws IOException {
        this.menu = menu;
        loadDeck();

        setLayout(null);
        setDefaultCloseOperation(3);
        switch(aiNumber){
            case 1: aiName="El Carpincho"; break;
            case 2: aiName="La Roca"; break;
            case 3: aiName="Messi"; break;
            case 4: aiName="El Diego"; break;
            case 5: aiName="Boris"; break;
            case 6: aiName="Guido"; break;
        }

        // Initializes the moving card
        movingCard.setOpaque(false);
        movingCard.setContentAreaFilled(false);
        movingCard.setBorderPainted(false);

        setBackground(0);
        background.setBounds(0, 0, 500, 800);
        background.setVisible(true);
        add(background);

        cardAi1 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardAi1.setBounds(100, 70, 75, 100);
        cardAi1.setVisible(false);
        background.add(cardAi1);

        cardAi2 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardAi2.setBounds(200, 70, 75, 100);
        cardAi2.setVisible(false);
        background.add(cardAi2);

        cardAi3 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardAi3.setBounds(300, 70, 75, 100);
        cardAi3.setVisible(false);
        background.add(cardAi3);

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

        cardThrownAi1 = new JLabel();
        cardThrownAi1.setBounds(130, 210, 70, 80);
        cardThrownAi1.setVisible(true);
        background.add(cardThrownAi1);

        cardThrownAi2 = new JLabel();
        cardThrownAi2.setBounds(210, 210, 70, 80);
        cardThrownAi2.setVisible(true);
        background.add(cardThrownAi2);

        cardThrownAi3 = new JLabel();
        cardThrownAi3.setBounds(290, 210, 70, 80);
        cardThrownAi3.setVisible(true);
        background.add(cardThrownAi3);

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
            JOptionPane.showMessageDialog(null, "Te has ido al mazo. Repartiendo...");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            int points=0;
            if(!finishedEnvido && ai.getPlayedCards().isEmpty())
                points++;
            ai.setPoints(ai.getPoints()+points+countPointsWonTruco(), this);
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
            try {
                askAiEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + aiName + ": " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
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
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            try {
                askAiEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + aiName + ": " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
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
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            try {
                askAiEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + aiName + ": " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
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
            envido.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                askAiEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + aiName + ": " + ex.getMessage());
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
            }
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
            if (player.calculateEnvido() > ai.calculateEnvido()) {
                ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                JOptionPane.showMessageDialog(null, "Has ganado. " + aiName + " tenía " + ai.calculateEnvido() + " de envido.");
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                player.setPoints(player.getPoints() + countPointsWonEnvido(ai.getPoints()), this);
            }
            else if (player.calculateEnvido() < ai.calculateEnvido()) {
                ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                JOptionPane.showMessageDialog(null, "Has perdido. " + aiName + " tenía " + ai.calculateEnvido() + " de envido.");
                effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                effects.play();
                ai.setPoints(ai.getPoints() + countPointsWonEnvido(player.getPoints()), this);
            }
            else if (player.calculateEnvido() == ai.calculateEnvido()) {
                if (player.isFirstHand() == true) {
                    ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                    JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    player.setPoints(player.getPoints() + countPointsWonEnvido(ai.getPoints()), this);
                } else {
                    ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                    JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + aiName + " es mano");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    ai.setPoints(ai.getPoints() + countPointsWonEnvido(player.getPoints()), this);
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
            ai.setPoints(ai.getPoints() + countPointsLoserEnvido(), this);

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
            finishedEnvido = true;
            if(enabledToRetrucar != 2){
                trucoLevel++;
                enabledToRetrucar = 2;
                try {
                    askAiTruco(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el truco de " + aiName + ": " + ex.getMessage());
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
            ai.setPoints(ai.getPoints() + countPointsLoseTruco(), this);
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

        JLabel pointsBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ aiNumber +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
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

            // If the game hasn't started
            if(dealCards.isEnabled()){
                menu.setVisible(true);
                finishedGame=true;
                dispose();
                return;
            }

            // If the game has started
            int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + aiName + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            if(dialogResult == JOptionPane.YES_OPTION)
                ai.setPoints(15, this);
        });
    }

    public void updatePoints() throws IOException {
        if(finishedGame)
            return;
        int jugadorPunt = player.getPoints();
        int aiPunt = ai.getPoints();
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

            JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó " + aiName + ". Será la próxima...");
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

    private void loadDeck() {
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

    private void mixDeck() {
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            ArrayList<Card> tempDeck = new ArrayList<>();
            for (int x = 0; x < deck.size() + tempDeck.size(); x++) {
                int posMezcla = random.nextInt(deck.size());
                tempDeck.add(deck.get(posMezcla));
                deck.remove(posMezcla);
            }
            deck.clear();
            deck.addAll(tempDeck);
        }
    }

    // TODO: TOMAR ESTA FUNCION Y COLOCARLA EN UNA INTERFÁZ
    // CONTINUAR MEJORANDO EL CODIGO: REEMPLAZAR SWTICHS CON FOR
    private void drawCards() throws IOException {

        ArrayList<JLabel> aiHand = new ArrayList<>();
        aiHand.add(cardAi1);
        aiHand.add(cardAi2);
        aiHand.add(cardAi3);
        for(int i=0;i<ai.getCards().size();i++)
            aiHand.get(i).setVisible(true);
        for(int i=ai.getCards().size();i<3;i++)
            aiHand.get(i).setVisible(false);

        ArrayList<JButton> playerHand = new ArrayList<>();
        playerHand.add(cardPlayer1);
        playerHand.add(cardPlayer2);
        playerHand.add(cardPlayer3);

        for(int i=0;i<3;i++){
            if(player.getPosCards()[i]<0){
                playerHand.get(i).setVisible(false);
                playerHand.get(i).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            } else {
                playerHand.get(i).setVisible(true);
                playerHand.get(i).setIcon(new ImageIcon(ImageIO.read(new File(player.getCards().get(player.getPosCards()[i]).linkCard())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            }
        }

        switch (ai.getPlayedCards().size()) {
            case 3:
                cardThrownAi1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownAi2.setIcon(new ImageIcon(ImageIO.read(new File(ai.getPlayedCards().get(1).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownAi3.setIcon(new ImageIcon(ImageIO.read(new File(ai.getPlayedCards().get(2).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                cardThrownAi1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownAi2.setIcon(new ImageIcon(ImageIO.read(new File(ai.getPlayedCards().get(1).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownAi3.setIcon(null);
                break;
            case 1:
                cardThrownAi1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getPlayedCards().get(0).linkCard())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                cardThrownAi2.setIcon(null);
                cardThrownAi3.setIcon(null);
                break;
            case 0:
                cardThrownAi1.setIcon(null);
                cardThrownAi2.setIcon(null);
                cardThrownAi3.setIcon(null);
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

    private void drawTrucoButton() throws IOException {
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
        // Intenta solucionar un bug de diferentes llamados que pueden perjudicar a la mano
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        if(!menu.fastModeCheckBox.isSelected()) movePlayerCard(pos, player.getCards().get(player.getPosCards()[pos]).linkCard(), player.getPlayedCards().size());

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

    private void anotherRound() throws IOException {
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
        drawTrucoButton();
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setVisible(true);
        setBackground(0);
        ai.setPlayersDeclaredEnvido(-1);

        // Clear hands
        player.setCards(new ArrayList<>());
        ai.setCards(new ArrayList<>());
        player.setPlayedCards(new ArrayList<>());
        ai.setPlayedCards(new ArrayList<>());

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

        if (ai.isFirstHand() == true) {
            ai.setCards(hand1);
            player.setCards(hand2);
            ai.setFirstHand(false);
            player.setFirstHand(true);
        } else {
            ai.setCards(hand2);
            player.setCards(hand1);
            ai.setFirstHand(true);
            player.setFirstHand(false);
        }

        updatePoints();
        drawCards();

    }

    private void updatesTurn() throws IOException {
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
            JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó " + aiName + ".");
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();

            ai.setPoints(ai.getPoints() + countPointsWonTruco(), this);
            anotherRound();
            updatesTurn();
            return;
        }

        if (player.getPlayedCards().isEmpty() && ai.getPlayedCards().isEmpty()) {
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
                ai.playTurnAlgorithm(player, this);
                moveAiCard(ai.getCards().size(), ai.getPlayedCards().size()-1);
            }
        } else if (player.getPlayedCards().isEmpty() && !ai.getPlayedCards().isEmpty()) {
            if(enabledToRetrucar < 2) truco.setEnabled(true);
            if(!finishedEnvido) envidoMenu.setEnabled(true);
            irAlMazo.setEnabled(true);
            cardPlayer1Enabled=true;
            cardPlayer2Enabled=true;
            cardPlayer3Enabled=true;
        } else if (!player.getPlayedCards().isEmpty() && ai.getPlayedCards().isEmpty()) {
                truco.setEnabled(false);
                envidoMenu.setEnabled(false);
                irAlMazo.setEnabled(false);
                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                // Not call askEnvidoToAI, because it's already inside of askTrucoToAI
                if (askAiTruco(false)==0);
                else return;
                ai.playTurnAlgorithm(player, this);
                moveAiCard(ai.getCards().size(), ai.getPlayedCards().size()-1);
        } else if (!player.getPlayedCards().isEmpty() && !ai.getPlayedCards().isEmpty()) {
            // If it's a round that nobody has played (start of the round)
            if (player.getPlayedCards().size() == ai.getPlayedCards().size()) {
                int playerRankingLastRound = player.getPlayedCards().get(player.getPlayedCards().size()-1).rankingCard();
                int aiRankingLastRound = ai.getPlayedCards().get(ai.getPlayedCards().size()-1).rankingCard();
                envidoMenu.setEnabled(false);

                if (playerRankingLastRound > aiRankingLastRound) {
                    if(enabledToRetrucar < 2) truco.setEnabled(true); // If the player can say 'truco'
                    // If the last card that the AI has thrown is a 4, player can't say 'truco'
                    if(ai.getPlayedCards().size() == 3) if(ai.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
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
                    ai.playTurnAlgorithm(player, this);
                    moveAiCard(ai.getCards().size(), ai.getPlayedCards().size()-1);
                } else if(playerRankingLastRound == aiRankingLastRound){
                    if(player.isFirstHand()){
                        if(enabledToRetrucar < 2) truco.setEnabled(true);
                        // If the last card that the AI has thrown is a 4, player can't say 'truco'
                        if(ai.getPlayedCards().size() == 3) if(ai.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
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
                        ai.playTurnAlgorithm(player, this);
                        moveAiCard(ai.getCards().size(), ai.getPlayedCards().size()-1);
                    }
                }
            } else if (player.getPlayedCards().size() == ai.getPlayedCards().size() - 1) {
                if(enabledToRetrucar < 2) truco.setEnabled(true);
                // If the last card that the AI has thrown is a 4, player can't say 'truco'
                if(ai.getPlayedCards().size() == 3) if(ai.getPlayedCards().get(2).rankingCard()==0) truco.setEnabled(false);
                irAlMazo.setEnabled(true);
                cardPlayer1Enabled=true;
                cardPlayer2Enabled=true;
                cardPlayer3Enabled=true;
            } else if (player.getPlayedCards().size() - 1 == ai.getPlayedCards().size()) {
                truco.setEnabled(false);
                envidoMenu.setEnabled(false);
                irAlMazo.setEnabled(false);
                cardPlayer1Enabled=false;
                cardPlayer2Enabled=false;
                cardPlayer3Enabled=false;
                if (askAiTruco(false)==0);
                else return;
                ai.playTurnAlgorithm(player, this);
                moveAiCard(ai.getCards().size(), ai.getPlayedCards().size()-1);
            }
        }
    }

    public int askAiEnvido() throws IOException {
        if (finishedEnvido == true)
            return 0;

        int decision;
        if (envidosDeclared.isEmpty())
            decision = ai.envidoAlgorithm(0, player, menu);

        else {
            decision = ai.envidoAlgorithm(envidosDeclared.get(envidosDeclared.size() - 1), player, menu);

            // AI Accepts
            if (decision == envidosDeclared.get(envidosDeclared.size() - 1)) {
                aiVoice.setFile("src/truco_java/cantos/envido/" + aiNumber + "5.wav", 1);
                aiVoice.play();

                if (player.calculateEnvido() > ai.calculateEnvido()) {
                    ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                    JOptionPane.showMessageDialog(null, "Has ganado. " + aiName + " tenía " + ai.calculateEnvido() + " de envido.");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    player.setPoints(player.getPoints() + countPointsWonEnvido(ai.getPoints()), this);
                }
                else if (player.calculateEnvido() < ai.calculateEnvido()) {
                    ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                    JOptionPane.showMessageDialog(null, "Has perdido. " + aiName + " tenía " + ai.calculateEnvido() + " de envido.");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    ai.setPoints(ai.getPoints() + countPointsWonEnvido(player.getPoints()), this);
                }
                else if (player.calculateEnvido() == ai.calculateEnvido()) {
                    if (player.isFirstHand() == true) {
                        ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has ganado por mano");
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                        player.setPoints(player.getPoints() + countPointsWonEnvido(ai.getPoints()), this);
                    } else {
                        ai.setPlayersDeclaredEnvido(player.calculateEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + player.calculateEnvido() + " de envido). Has perdido, " + aiName + " es mano");
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                        ai.setPoints(ai.getPoints() + countPointsWonEnvido(player.getPoints()), this);
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

        if(envidosDeclared.size() == 1)
            total+=1;

        finishedEnvido = true;
        return total;
    }

    private void printsEnvidoMessage(int envido, boolean isCalledFromTimer){
        String text = " ";
        statusBackground.setVisible(true);
        status.setFont(new Font("Serif", Font.ITALIC, 30));
        status.setVisible(true);

        if(!isCalledFromTimer && envido!=0) {
            aiVoice.setFile("src/truco_java/cantos/envido/" + aiNumber + envido + ".wav", 1);
            aiVoice.play();
        }

        switch(envido){
            case -1:
                text = "No quiero!";
                if(aiNumber==5) text = "No tea? so No quiero!";
                break;
            case 0:
                statusBackground.setVisible(false);
                status.setText("");
                setBackground(0);
                return;
            case 1:
                text = "Envido!";
                if(aiNumber==5) text = "Why not? Envido!";
                break;
            case 2:
                text = "Envido!";
                if(aiNumber==5) {
                    text = "What about another Envido?!";
                    status.setFont(new Font("Serif", Font.ITALIC, 25));
                }
                break;
            case 3:
                text = "Real Envido!";
                if(aiNumber==5) text = "I say Real Envido!";
                break;
            case 4:
                text = "Falta Envido!";
                if(aiNumber==5) text = "Falta Envido my mate!";
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
        if(player.getPlayedCards().size() != ai.getPlayedCards().size() || ai.getPlayedCards().isEmpty())
            return 0;

        int roundsWonPlayer=0, roundsWonAi=0, winner=0;
        boolean tieDefines=false;

        for(int i=0;i<player.getPlayedCards().size();i++){
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
        int rankingAI = ai.getPlayedCards().get(roundNumber).rankingCard();

        if(rankingJugador > rankingAI)
            return 1;
        if(rankingJugador < rankingAI)
            return 2;

        return 0;
    }

    // The argument 'hasToAnswer' is a boolean that indicates if the user sang 'truco', so, that way the AI can say 'No quiero' to what the user said
    public int askAiTruco(boolean hasToAnswer) throws IOException {
        // If it returns 0, that means not do anything
        // If it returns 1, that means that the user has to response (stop normal
        //  execution of the game till the user responds)
        if (enabledToRetrucar == 1)
            return 0;

        if(ai.getPlayedCards().size() == 3 && ai.getPlayedCards().get(2).rankingCard()==0) {
                truco.setEnabled(false);
                return 0;
        }
        if(player.getPlayedCards().size() == 3 && player.getPlayedCards().get(2).rankingCard()==0)
                return 0;

        if(!finishedEnvido && ai.getPlayedCards().isEmpty()){
            if(askAiEnvido()!=0){
                if(trucoLevel!=0) trucoLevel--;
                enabledToRetrucar = 0;
                return 1;
            }
        }

        int decision = ai.trucoAlgorithm(trucoLevel, player, menu);

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
                JOptionPane.showMessageDialog(null, "" + aiName + " ha rechazado el Truco. Repartiendo...");
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
            JOptionPane.showMessageDialog(null, "" + aiName + " ha rechazado el Truco. Repartiendo...");
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

    private void printsTrucoMessage(int trucoMessage, boolean isCalledFromTimer) throws IOException{
        setBackground(1);
        statusBackground.setVisible(true);

        if(!isCalledFromTimer && trucoMessage!=0) {
            aiVoice.setFile("src/truco_java/cantos/truco/" + aiNumber + trucoMessage + ".wav", 1);
            aiVoice.play();
        }

        // TODO: Put every `status.setText("xxxxxxx")` in a variable `text`, that after the switch calls the status.setText("xxxxxxx"). And update the `timerText` initialization
        switch(trucoMessage){
            case -1:
                status.setText("No quiero!");
                if(aiNumber==5) status.setText("This’s a rip-off. No quiero");
                break;
            case 0:
                statusBackground.setVisible(false);
                status.setText("");
                setBackground(0);
                return;
            case 1:
                status.setText("Truco!");
                if(aiNumber==5) status.setText("Come on, Truco!");
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/retrucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                truco.setEnabled(true);
                break;
            case 2:
                status.setText("Re truco!");
                if(aiNumber==5) status.setText("Re truco if you're brave!");
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/valeCuatroBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                truco.setEnabled(true);
                break;
            case 3:
                status.setText("Quiero vale 4!");
                if(aiNumber==5) status.setText("Really? Quiero vale cuatro");
                truco.setVisible(false);
                break;
            case 4:
                status.setText("Quiero!");
                if(aiNumber==5) status.setText("Easy peasy. Quiero!");
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
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + aiName + " en el truco: " + ex.getMessage());
                                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    effects.play();
                                }
                            }
                            else {
                                try {
                                    printsTrucoMessage(0, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + aiName + " en el truco: " + ex.getMessage());
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

    private int countPointsWonTruco() {
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

    private int countPointsLoseTruco() {
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

        String image = "src/truco_java/fondos/bg" + aiNumber + statusChar + ".png";
        background.setIcon(new ImageIcon(image));
    }

    private void moveAiCard(int origin, int destination) throws IOException{
        final int originX, destinationX;
        final String file = ai.getPlayedCards().get(ai.getPlayedCards().size()-1).linkCard();

        // Depending on what card is, it hides it and puts a temporal one in replace
        switch(origin){
            case 0:
                cardAi1.setVisible(false);
                originX=100;
                break;
            case 1:
                cardAi2.setVisible(false);
                originX=180;
                break;
            case 2:
                cardAi3.setVisible(false);
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
        final int moveX=(destinationX-originX)/movementPercentage, movY=(210-70)/movementPercentage;
        final int sizeX = (movingCard.getWidth()-70)/movementPercentage;
        final int sizeY = (movingCard.getHeight()-80)/movementPercentage;

        Thread thread = new Thread(){
            public void run(){
                moveCardTimer(moveX, movY, originX, 70, destinationX, 210, sizeX, sizeY, file);
            }
        };
        thread.start();
    }

    private void movePlayerCard(int origin, String file, int destination) throws IOException{
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

  public void moveCardTimer (int moveX, int moveY, int origenX, int originY, int destinationX, int destinationY, int width, int height, String file) {
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
