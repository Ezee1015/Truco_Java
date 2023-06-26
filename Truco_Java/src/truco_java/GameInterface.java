package truco_java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.Image;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public abstract class GameInterface extends JFrame{

    // Swing
    protected JLabel background = new JLabel();
    protected JLabel cardOpponent1, cardOpponent2, cardOpponent3;
    protected JLabel cardThrownOpponent1, cardThrownOpponent2, cardThrownOpponent3;
    protected JButton cardPlayer1, cardPlayer2, cardPlayer3;
    protected JLabel cardThrownPlayer1, cardThrownPlayer2, cardThrownPlayer3;
    protected JButton truco, envidoMenu, irAlMazo, envido, envidoEnvido, realEnvido, faltaEnvido;
    protected JButton noQuieroEnvido, quieroEnvido, noQuieroTruco, quieroTruco;
    protected JLabel statusBackground;
    protected JLabel pointsAi = new JLabel(), pointsPlayer = new JLabel();
    protected JTextPane status;
    protected JButton movingCard = new JButton();
    protected boolean cardPlayer1Enabled=false, cardPlayer2Enabled=false, cardPlayer3Enabled=false;
    protected JLabel connectionBackground = new JLabel(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
    protected JLabel pointsBackground = new JLabel();
    protected JFrame window = this;
    protected JButton dealCards;

    protected Truco_Java menu;
    protected PlayMenu playMenu;
    protected final Music opponentVoice = new Music();
    protected static final Music effects = new Music();
    protected boolean finishedGame = false;
    protected ArrayList<Integer> envidosDeclared = new ArrayList<>();
    protected int trucoLevel;
    protected boolean finishedEnvido;
    protected int enabledToRetrucar; // 2--> Player; 1--> opponent

    protected Player player   = new Player();
    protected Player opponent = new Player();
    protected int opponentNumber; // Represents the player that was selected
    protected String opponentName ="la PC";
    protected String playerName = "the Player";

    protected abstract void backAction(ActionEvent e);
    protected abstract void irAlMazoAction (ActionEvent e);
    protected abstract void envidoAction (ActionEvent e);
    protected abstract void envidoEnvidoAction (ActionEvent e);
    protected abstract void realEnvidoAction (ActionEvent e);
    protected abstract void faltaEnvidoAction (ActionEvent e);
    protected abstract void quieroEnvidoAction (ActionEvent e);
    protected abstract void noQuieroEnvidoAction (ActionEvent e);
    protected abstract void trucoAction (ActionEvent e);
    protected abstract void quieroTrucoAction (ActionEvent e);
    protected abstract void noQuieroTrucoAction (ActionEvent e);

    protected abstract void actionAfterThrowingCard(boolean isThePlayer, int posCardThrown);
    // TODO: make this function not abstract
    protected abstract void updatePoints() throws IOException;
    protected abstract void loadPlayersName();

    protected GameInterface(Truco_Java menu, PlayMenu playMenu) throws IOException{
        this.menu = menu;
        this.playMenu = playMenu;

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
        cardOpponent1.setVisible(false);
        background.add(cardOpponent1);

        cardOpponent2 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardOpponent2.setBounds(200, 70, 75, 100);
        cardOpponent2.setVisible(false);
        background.add(cardOpponent2);

        cardOpponent3 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        cardOpponent3.setBounds(300, 70, 75, 100);
        cardOpponent3.setVisible(false);
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
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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
            connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
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

        dealCards = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cartasMazo.png")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        dealCards.setBounds(365, 280, 80, 80);
        dealCards.setBorderPainted(false);
        dealCards.setOpaque(false);
        dealCards.setContentAreaFilled(false);
        dealCards.setBorderPainted(false);
        dealCards.setVisible(true);
        dealCards.setEnabled(false);
        background.add(dealCards);

        cardThrownOpponent1 = new JLabel();
        cardThrownOpponent1.setBounds(120, 210, 70, 80);
        cardThrownOpponent1.setVisible(true);
        background.add(cardThrownOpponent1);

        cardThrownOpponent2 = new JLabel();
        cardThrownOpponent2.setBounds(200, 210, 70, 80);
        cardThrownOpponent2.setVisible(true);
        background.add(cardThrownOpponent2);

        cardThrownOpponent3 = new JLabel();
        cardThrownOpponent3.setBounds(280, 210, 70, 80);
        cardThrownOpponent3.setVisible(true);
        background.add(cardThrownOpponent3);

        cardThrownPlayer1 = new JLabel();
        cardThrownPlayer1.setBounds(120, 310, 70, 80);
        cardThrownPlayer1.setVisible(true);
        background.add(cardThrownPlayer1);

        cardThrownPlayer2 = new JLabel();
        cardThrownPlayer2.setBounds(200, 310, 70, 80);
        cardThrownPlayer2.setVisible(true);
        background.add(cardThrownPlayer2);

        cardThrownPlayer3 = new JLabel();
        cardThrownPlayer3.setBounds(280, 310, 70, 80);
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
        irAlMazo.addActionListener(this::irAlMazoAction);

        envido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envido.setBounds(10, 595, 240, 50);
        envido.setVisible(false);
        envido.setOpaque(false);
        envido.setContentAreaFilled(false);
        envido.setBorderPainted(false);
        background.add(envido);
        envido.addActionListener(this::envidoAction);

        envidoEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envidoEnvido.setBounds(10, 595, 240, 50);
        envidoEnvido.setVisible(false);
        envidoEnvido.setOpaque(false);
        envidoEnvido.setContentAreaFilled(false);
        envidoEnvido.setBorderPainted(false);
        background.add(envidoEnvido);
        envidoEnvido.addActionListener(this::envidoEnvidoAction);

        realEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/realEnvidoBoton.png")).getScaledInstance(117, 50, Image.SCALE_SMOOTH)));
        realEnvido.setBounds(252, 595, 117, 50);
        realEnvido.setVisible(false);
        realEnvido.setOpaque(false);
        realEnvido.setContentAreaFilled(false);
        realEnvido.setBorderPainted(false);
        background.add(realEnvido);
        realEnvido.addActionListener(this::realEnvidoAction);

        faltaEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/faltaEnvidoBoton.png")).getScaledInstance(117, 50, Image.SCALE_SMOOTH)));
        faltaEnvido.setBounds(373, 595, 117, 50);
        faltaEnvido.setVisible(false);
        faltaEnvido.setOpaque(false);
        faltaEnvido.setContentAreaFilled(false);
        faltaEnvido.setBorderPainted(false);
        background.add(faltaEnvido);
        faltaEnvido.addActionListener(this::faltaEnvidoAction);

        quieroEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/quieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        quieroEnvido.setBounds(10, 250, 110, 40);
        quieroEnvido.setVisible(false);
        quieroEnvido.setOpaque(false);
        quieroEnvido.setContentAreaFilled(false);
        quieroEnvido.setBorderPainted(false);
        background.add(quieroEnvido);
        quieroEnvido.addActionListener(this::quieroEnvidoAction);

        noQuieroEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/noQuieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        noQuieroEnvido.setBounds(10, 300, 110, 40);
        noQuieroEnvido.setVisible(false);
        noQuieroEnvido.setOpaque(false);
        noQuieroEnvido.setContentAreaFilled(false);
        noQuieroEnvido.setBorderPainted(false);
        background.add(noQuieroEnvido);
        noQuieroEnvido.addActionListener(this::noQuieroEnvidoAction);

        truco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setBounds(10, 660, 155, 60);
        truco.setVisible(true);
        truco.setEnabled(false);
        truco.setOpaque(false);
        truco.setContentAreaFilled(false);
        truco.setBorderPainted(false);
        background.add(truco);
        truco.addActionListener(this::trucoAction);

        quieroTruco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/quieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        quieroTruco.setBounds(10, 250, 110, 40);
        quieroTruco.setVisible(false);
        quieroTruco.setOpaque(false);
        quieroTruco.setContentAreaFilled(false);
        quieroTruco.setBorderPainted(false);
        background.add(quieroTruco);
        quieroTruco.addActionListener(this::quieroTrucoAction);


        noQuieroTruco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/noQuieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        noQuieroTruco.setBounds(10, 300, 110, 40);
        noQuieroTruco.setVisible(false);
        noQuieroTruco.setOpaque(false);
        noQuieroTruco.setContentAreaFilled(false);
        noQuieroTruco.setBorderPainted(false);
        background.add(noQuieroTruco);
        noQuieroTruco.addActionListener(this::noQuieroTrucoAction);

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
        back.addActionListener(this::backAction);

        connectionBackground.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        connectionBackground.setBounds(0,730,500,50);
        connectionBackground.setOpaque(false);
        connectionBackground.setVisible(true);
        background.add(connectionBackground);
    }


    protected void drawButtons() throws IOException {
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

    protected void setBackground(int status){
        char statusChar = status==0 ? 'a' : 'b';
        // a ==> Normal Background
        // b ==> Expression Background

        String image = "src/truco_java/fondos/bg" + opponentNumber + statusChar + ".png";
        background.setIcon(new ImageIcon(image));
    }

    protected void printsTrucoMessage(int trucoMessage, boolean isCalledFromTimer) throws IOException{
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

    protected void printsEnvidoMessage(int envido, boolean isCalledFromTimer){
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

    protected void drawCards() throws IOException {
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

    protected void throwCard(int pos) throws IOException {
        cardPlayer1Enabled=false;
        cardPlayer2Enabled=false;
        cardPlayer3Enabled=false;

        player.addPlayedCards(player.getPosCards()[pos]);

        // Indicates which card should not be drawn
        int temp[] = player.getPosCards();
        temp[pos] = -1;
        for(int i=pos+1;i<temp.length;i++)
            temp[i]-=1;
        player.setPosCards(temp);

        if(menu.fastModeCheckBox.isSelected())
            actionAfterThrowingCard(true, pos);
        else
            moveCard(pos, player.getPlayedCards().size()-1, player, true);
    }

    protected void moveCard(int origin, int destination, Player playerToMove, boolean isThePlayer) throws IOException{
        final String file = playerToMove.getPlayedCards().get(playerToMove.getPlayedCards().size()-1).linkCard();
        final int originY          = isThePlayer ? cardPlayer1.getY() : cardOpponent1.getY();
        final int destinationY     = isThePlayer ? cardThrownPlayer1.getY() : cardThrownOpponent1.getY();
        final int movingCardWidth  = isThePlayer ? cardPlayer1.getWidth() : cardOpponent1.getWidth();
        final int movingCardHeight = isThePlayer ? cardPlayer1.getHeight() : cardOpponent1.getHeight();
        final int originX;
        final int destinationX;

        // Depending on what card is, it hides it and puts a temporal one in replace
        JComponent cardOrigin = new JButton();
        switch(origin){
            case 0:
                cardOrigin = isThePlayer ? cardPlayer1 : cardOpponent1;
                break;
            case 1:
                cardOrigin = isThePlayer ? cardPlayer2 : cardOpponent2;
                break;
            case 2:
                cardOrigin = isThePlayer ? cardPlayer3 : cardOpponent3;
                break;
            default:
                return;
        }
        cardOrigin.setVisible(false);
        originX=cardOrigin.getX();

        switch(destination){
            case 0:
                destinationX= isThePlayer ? cardThrownPlayer1.getX() : cardThrownOpponent1.getX();
                break;
            case 1:
                destinationX= isThePlayer ? cardThrownPlayer2.getX() : cardThrownOpponent2.getX();
                break;
            case 2:
                destinationX= isThePlayer ? cardThrownPlayer3.getX() : cardThrownOpponent3.getX();
                break;
            default:
                return;
        }

        movingCard.setBounds(originX, originY, movingCardWidth, movingCardHeight);
        movingCard.setVisible(true);
        background.add(movingCard);

        // TODO: Make an slider in the GUI for adjusting the movement percentage
        int movementPercentage=30;
        final float moveX=(float)(destinationX-originX)/movementPercentage;
        final float moveY=(float)(destinationY-originY)/movementPercentage;
        final float sizeX = (float)(movingCardWidth-cardThrownPlayer1.getWidth()) / movementPercentage;
        final float sizeY = (float)(movingCardHeight-cardThrownPlayer1.getHeight()) / movementPercentage;

        Thread thread = new Thread(){
            public void run(){
                moveCardTimer(moveX, moveY, sizeX, sizeY, movementPercentage, file);
                actionAfterThrowingCard(isThePlayer, origin);
            }
        };
        thread.start();
    }

    protected void moveCardTimer (float moveX, float moveY, float widthDecrease, float heightDecrease, int percentageMovement, String file) {
        final int movingCardWidthOrigin = movingCard.getWidth();
        final int movingCardHeightOrigin = movingCard.getHeight();
        final int originX = movingCard.getX();
        final int originY = movingCard.getY();

        for(int i=0;i<percentageMovement;i++){
            final int movingCardX = (int)(originX+moveX*i);
            final int movingCardY = (int)(originY+moveY*i);
            final int movingCardWidth  = (int)(movingCardWidthOrigin-(widthDecrease*i));
            final int movingCardHeight = (int)(movingCardHeightOrigin-(heightDecrease*i));

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            movingCard.setBounds(movingCardX, movingCardY, movingCardWidth, movingCardHeight);

                            try {
                                movingCard.setIcon(new ImageIcon(ImageIO.read(new File(file)).getScaledInstance(movingCardWidth, movingCardHeight, Image.SCALE_SMOOTH)));
                                movingCard.repaint();
                            } catch (IOException e) {
                            }

                        }
                    },
                    1
                    );

            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (Exception e) {
            }
        }

        // Enables to play
        movingCard.setVisible(false);
        background.remove(movingCard);

    }

}
