package truco_java;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

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
    protected abstract void actionWhenPlayerWinsGame();
    protected abstract void actionWhenOpponentWinsGame();
    protected abstract void loadPlayersName();

    protected GameInterface(Truco_Java menu, PlayMenu playMenu){
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

        cardOpponent1 = new JLabel(getImageIcon("src/truco_java/mazo/reverso.png", 75, 100, false));
        cardOpponent1.setBounds(100, 70, 75, 100);
        cardOpponent1.setVisible(false);
        background.add(cardOpponent1);

        cardOpponent2 = new JLabel(getImageIcon("src/truco_java/mazo/reverso.png", 75, 100, false));
        cardOpponent2.setBounds(200, 70, 75, 100);
        cardOpponent2.setVisible(false);
        background.add(cardOpponent2);

        cardOpponent3 = new JLabel(getImageIcon("src/truco_java/mazo/reverso.png", 75, 100, false));
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
            throwCard(0);
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
            throwCard(1);
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
            throwCard(2);
            setBackground(0);
        });

        dealCards = new JButton(getImageIcon("src/truco_java/fondos/cartasMazo.png", 80, 80, false));
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

        envidoMenu = new JButton(getImageIcon("src/truco_java/fondos/envidoBoton.png", 155, 60, false));
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

        irAlMazo = new JButton(getImageIcon("src/truco_java/fondos/irAlMazoBoton.png", 155, 60, false));
        irAlMazo.setBounds(330, 660, 155, 60);
        irAlMazo.setVisible(true);
        irAlMazo.setEnabled(false);
        irAlMazo.setOpaque(false);
        irAlMazo.setContentAreaFilled(false);
        irAlMazo.setBorderPainted(false);
        background.add(irAlMazo);
        irAlMazo.addActionListener(this::irAlMazoAction);

        envido = new JButton(getImageIcon("src/truco_java/fondos/envidoGrandeBoton.png", 240, 50, false));
        envido.setBounds(10, 595, 240, 50);
        envido.setVisible(false);
        envido.setOpaque(false);
        envido.setContentAreaFilled(false);
        envido.setBorderPainted(false);
        background.add(envido);
        envido.addActionListener(this::envidoAction);

        envidoEnvido = new JButton(getImageIcon("src/truco_java/fondos/envidoGrandeBoton.png", 240, 50, false));
        envidoEnvido.setBounds(10, 595, 240, 50);
        envidoEnvido.setVisible(false);
        envidoEnvido.setOpaque(false);
        envidoEnvido.setContentAreaFilled(false);
        envidoEnvido.setBorderPainted(false);
        background.add(envidoEnvido);
        envidoEnvido.addActionListener(this::envidoEnvidoAction);

        realEnvido = new JButton(getImageIcon("src/truco_java/fondos/realEnvidoBoton.png", 117, 50, false));
        realEnvido.setBounds(252, 595, 117, 50);
        realEnvido.setVisible(false);
        realEnvido.setOpaque(false);
        realEnvido.setContentAreaFilled(false);
        realEnvido.setBorderPainted(false);
        background.add(realEnvido);
        realEnvido.addActionListener(this::realEnvidoAction);

        faltaEnvido = new JButton(getImageIcon("src/truco_java/fondos/faltaEnvidoBoton.png", 117, 50, false));
        faltaEnvido.setBounds(373, 595, 117, 50);
        faltaEnvido.setVisible(false);
        faltaEnvido.setOpaque(false);
        faltaEnvido.setContentAreaFilled(false);
        faltaEnvido.setBorderPainted(false);
        background.add(faltaEnvido);
        faltaEnvido.addActionListener(this::faltaEnvidoAction);

        quieroEnvido = new JButton(getImageIcon("src/truco_java/fondos/quieroBoton.png", 110, 40, false));
        quieroEnvido.setBounds(10, 250, 110, 40);
        quieroEnvido.setVisible(false);
        quieroEnvido.setOpaque(false);
        quieroEnvido.setContentAreaFilled(false);
        quieroEnvido.setBorderPainted(false);
        background.add(quieroEnvido);
        quieroEnvido.addActionListener(this::quieroEnvidoAction);

        noQuieroEnvido = new JButton(getImageIcon("src/truco_java/fondos/noQuieroBoton.png", 110, 40, false));
        noQuieroEnvido.setBounds(10, 300, 110, 40);
        noQuieroEnvido.setVisible(false);
        noQuieroEnvido.setOpaque(false);
        noQuieroEnvido.setContentAreaFilled(false);
        noQuieroEnvido.setBorderPainted(false);
        background.add(noQuieroEnvido);
        noQuieroEnvido.addActionListener(this::noQuieroEnvidoAction);

        truco = new JButton(getImageIcon("src/truco_java/fondos/trucoBoton.png", 155, 60, false));
        truco.setBounds(10, 660, 155, 60);
        truco.setVisible(true);
        truco.setEnabled(false);
        truco.setOpaque(false);
        truco.setContentAreaFilled(false);
        truco.setBorderPainted(false);
        background.add(truco);
        truco.addActionListener(this::trucoAction);

        quieroTruco = new JButton(getImageIcon("src/truco_java/fondos/quieroBoton.png", 110, 40, false));
        quieroTruco.setBounds(10, 250, 110, 40);
        quieroTruco.setVisible(false);
        quieroTruco.setOpaque(false);
        quieroTruco.setContentAreaFilled(false);
        quieroTruco.setBorderPainted(false);
        background.add(quieroTruco);
        quieroTruco.addActionListener(this::quieroTrucoAction);


        noQuieroTruco = new JButton(getImageIcon("src/truco_java/fondos/noQuieroBoton.png", 110, 40, false));
        noQuieroTruco.setBounds(10, 300, 110, 40);
        noQuieroTruco.setVisible(false);
        noQuieroTruco.setOpaque(false);
        noQuieroTruco.setContentAreaFilled(false);
        noQuieroTruco.setBorderPainted(false);
        background.add(noQuieroTruco);
        noQuieroTruco.addActionListener(this::noQuieroTrucoAction);

        pointsBackground = new JLabel(getImageIcon("src/truco_java/puntaje/bg"+ opponentNumber +".png", 100, 150, false));
        pointsBackground.setBounds(390, 10, 100, 150);
        pointsBackground.setVisible(true);
        background.add(pointsBackground);

        pointsPlayer.setBounds(0, 65, 50, 85);
        pointsPlayer.setVisible(true);
        pointsBackground.add(pointsPlayer);

        pointsAi.setBounds(50, 65, 50, 85);
        pointsAi.setVisible(true);
        pointsBackground.add(pointsAi);

        JButton back = new JButton(getImageIcon("src/truco_java/fondos/atras.png", 50, 50, false));
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


    protected void drawButtons(){
        switch (trucoLevel) {
            case 0:
                truco.setIcon(getImageIcon("src/truco_java/fondos/trucoBoton.png", 155, 60, false));
                break;
            case 1:
                truco.setIcon(getImageIcon("src/truco_java/fondos/retrucoBoton.png", 155, 60, false));
                break;
            case 2:
                truco.setIcon(getImageIcon("src/truco_java/fondos/valeCuatroBoton.png", 155, 60, false));
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

    protected void printsTrucoMessage(int trucoMessage, boolean isCalledFromTimer){
        final String timerText;

        statusBackground.setVisible(true);

        if(!isCalledFromTimer && trucoMessage!=0) {
            opponentVoice.setFile("src/truco_java/cantos/truco/" + opponentNumber + trucoMessage + ".wav", 1);
            opponentVoice.play();
        }

        switch(trucoMessage){
            case -1:
                timerText = opponentNumber==5 ? "This’s a rip-off. No quiero" : "No quiero!";
                break;
            case 0:
                statusBackground.setVisible(false);
                timerText = "";
                setBackground(0);
                return;
            case 1:
                timerText = opponentNumber==5 ? "Come on, Truco!" : "Truco!";
                truco.setIcon(getImageIcon("src/truco_java/fondos/retrucoBoton.png", 155, 60, false));
                truco.setEnabled(true);
                break;
            case 2:
                timerText = opponentNumber==5 ? "Re truco if you're brave!" : "Re truco!";
                truco.setIcon(getImageIcon("src/truco_java/fondos/valeCuatroBoton.png", 155, 60, false));
                truco.setEnabled(true);
                break;
            case 3:
                timerText = opponentNumber==5 ? "Really? Quiero vale cuatro" : "Quiero vale 4!";
                truco.setEnabled(false);
                break;
            case 4:
                timerText = opponentNumber==5 ? "Easy peasy. Quiero!" : "Quiero!";
                truco.setEnabled(false);
                break;
            default:
                timerText = "";
                break;
        }

        finishedEnvido = true;
        status.setText(timerText);
        setBackground(1);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if(status.getText().equals(timerText)){
                            if(quieroTruco.isVisible()){
                                printsTrucoMessage(trucoMessage, true);
                            }
                            else {
                                printsTrucoMessage(0, true);
                            }
                        }
                    }
                },
                2000
                );
    }

    protected void printsEnvidoMessage(int envido, boolean isCalledFromTimer){
        final String text;
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
                text = opponentNumber==5 ? "No tea? so No quiero!" : "No quiero!";
                break;
            case 0:
                statusBackground.setVisible(false);
                text = "";
                setBackground(0);
                return;
            case 1:
                text = opponentNumber==5 ? "Why not? Envido!" : "Envido!";
                break;
            case 2:
                text = opponentNumber==5 ? "What about another Envido?!" : "Envido!";
                status.setFont(new Font("Serif", Font.ITALIC, 25));
                break;
            case 3:
                text = opponentNumber==5 ? "I say Real Envido!" : "Real Envido!";
                break;
            case 4:
                text = opponentNumber==5 ? "Falta Envido my mate!" : "Falta Envido!";
                break;
            case 5:
                text = opponentNumber==5 ? "Easy peasy. Quiero!" : "Quiero!";
                break;
            default:
                text="";
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

    protected ImageIcon getImageIcon(String url, int resizeX, int resizeY, boolean mirror){
        try{
            if(!mirror)
                return new ImageIcon(ImageIO.read(new File(url)).getScaledInstance(resizeX, resizeY, Image.SCALE_SMOOTH));

            ImageIcon originalIcon = new ImageIcon("src/truco_java/fondos/cartasMazo.png");
            Image originalImage = originalIcon.getImage();

            int width = originalImage.getWidth(null);
            int height = originalImage.getHeight(null);

            BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = flippedImage.createGraphics();
            AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
            transform.translate(-width, 0);
            g2d.drawImage(originalImage, transform, null);
            g2d.dispose();

            Image scaledFlippedImage = flippedImage.getScaledInstance(resizeX, resizeY, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledFlippedImage);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + e.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            return null;
        }
    }

    protected ImageIcon getImageIcon(String url, boolean mirror){
        try{
            if(!mirror)
                return new ImageIcon(ImageIO.read(new File(url)));

            ImageIcon originalIcon = new ImageIcon("src/truco_java/fondos/cartasMazo.png");
            Image originalImage = originalIcon.getImage();

            int width = originalImage.getWidth(null);
            int height = originalImage.getHeight(null);

            BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = flippedImage.createGraphics();
            AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
            transform.translate(-width, 0);
            g2d.drawImage(originalImage, transform, null);
            g2d.dispose();
            return new ImageIcon(flippedImage);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + e.getMessage());
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            return null;
        }
    }

    protected void drawCards() {

        if(player.getCards().size()==3){
            if(player.isFirstHand()){
                dealCards.setLocation(36,280);
                dealCards.setIcon(getImageIcon("src/truco_java/fondos/cartasMazo.png", 80, 80, true));

                quieroTruco.setBounds(375, 250, 110, 40);
                noQuieroTruco.setBounds(375, 300, 110, 40);
                quieroEnvido.setBounds(375, 250, 110, 40);
                noQuieroEnvido.setBounds(375, 300, 110, 40);
            } else {
                dealCards.setLocation(365,280);
                dealCards.setIcon(getImageIcon("src/truco_java/fondos/cartasMazo.png", 80, 80, false));

                quieroTruco.setBounds(10, 250, 110, 40);
                noQuieroTruco.setBounds(10, 300, 110, 40);
                quieroEnvido.setBounds(10, 250, 110, 40);
                noQuieroEnvido.setBounds(10, 300, 110, 40);
            }
        }

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
                manos.get(i).setIcon(getImageIcon("src/truco_java/mazo/reverso.png", 155, 200, false));
            } else {
                manos.get(i).setVisible(true);
                manos.get(i).setIcon(getImageIcon(player.getCards().get(player.getPosCards()[i]).linkCard(), 155, 200, false));
            }
        }

        switch (opponent.getPlayedCards().size()) {
            case 3:
                cardThrownOpponent1.setIcon(getImageIcon(opponent.getPlayedCards().get(0).linkCard(), 70, 80, false));
                cardThrownOpponent2.setIcon(getImageIcon(opponent.getPlayedCards().get(1).linkCard(), 70, 80, false));
                cardThrownOpponent3.setIcon(getImageIcon(opponent.getPlayedCards().get(2).linkCard(), 70, 80, false));
                break;
            case 2:
                cardThrownOpponent1.setIcon(getImageIcon(opponent.getPlayedCards().get(0).linkCard(), 70, 80, false));
                cardThrownOpponent2.setIcon(getImageIcon(opponent.getPlayedCards().get(1).linkCard(), 70, 80, false));
                cardThrownOpponent3.setIcon(null);
                break;
            case 1:
                cardThrownOpponent1.setIcon(getImageIcon(opponent.getPlayedCards().get(0).linkCard(), 70, 80, false));
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
                cardThrownPlayer1.setIcon(getImageIcon(player.getPlayedCards().get(0).linkCard(), 70, 80, false));
                cardThrownPlayer2.setIcon(getImageIcon(player.getPlayedCards().get(1).linkCard(), 70, 80, false));
                cardThrownPlayer3.setIcon(getImageIcon(player.getPlayedCards().get(2).linkCard(), 70, 80, false));
                break;
            case 2:
                cardThrownPlayer1.setIcon(getImageIcon(player.getPlayedCards().get(0).linkCard(), 70, 80, false));
                cardThrownPlayer2.setIcon(getImageIcon(player.getPlayedCards().get(1).linkCard(), 70, 80, false));
                cardThrownPlayer3.setIcon(null);
                break;
            case 1:
                cardThrownPlayer1.setIcon(getImageIcon(player.getPlayedCards().get(0).linkCard(), 70, 80, false));
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

    protected void throwCard(int pos){
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

        moveCard(pos, player.getPlayedCards().size()-1, player, true);
    }

    protected void moveCard(int origin, int destination, Player playerToMove, boolean isThePlayer){
        if(menu.fastModeCheckBox.isSelected()){
            actionAfterThrowingCard(isThePlayer, origin);
            return;
        }
        //
        // TODO: Make an slider in the GUI for adjusting the movement percentage. But
        // for that, I need to create a config class, where should live all the
        // configurations of the game (and the option to delete and change password in a
        // account)
        final int frames=30;
        final int sleepPerFrame=10;

        final String file = playerToMove.getPlayedCards().get(playerToMove.getPlayedCards().size()-1).linkCard();
        final int originY          = isThePlayer ? cardPlayer1.getY() : cardOpponent1.getY();
        final int destinationY     = isThePlayer ? cardThrownPlayer1.getY() : cardThrownOpponent1.getY();
        final int movingCardWidth  = isThePlayer ? cardPlayer1.getWidth() : cardOpponent1.getWidth();
        final int movingCardHeight = isThePlayer ? cardPlayer1.getHeight() : cardOpponent1.getHeight();
        final int thrownCardHeight = cardThrownPlayer1.getHeight();
        final int thrownCardWidth = cardThrownPlayer1.getHeight();
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

        final float movementStepX=(float)(destinationX-originX) / frames;
        final float movementStepY=(float)(destinationY-originY) / frames;
        final float sizeStepX = (float)(movingCardWidth-thrownCardWidth)   / frames;
        final float sizeStepY = (float)(movingCardHeight-thrownCardHeight) / frames;

        Thread thread = new Thread(){
            public void run(){
                moveCardTimer(movementStepX, movementStepY, sizeStepX, sizeStepY, frames, sleepPerFrame, file);
                actionAfterThrowingCard(isThePlayer, origin);
            }
        };
        thread.start();
    }

    private void moveCardTimer (float moveX, float moveY, float widthDecrease, float heightDecrease, int frames, int sleepPerFrame, String file) {
        final int movingCardWidthOrigin = movingCard.getWidth();
        final int movingCardHeightOrigin = movingCard.getHeight();
        final int originX = movingCard.getX();
        final int originY = movingCard.getY();

        for(int i=0;i<frames;i++){
            final int movingCardX = (int)(originX+moveX*i);
            final int movingCardY = (int)(originY+moveY*i);
            final int movingCardWidth  = (int)(movingCardWidthOrigin-(widthDecrease*i));
            final int movingCardHeight = (int)(movingCardHeightOrigin-(heightDecrease*i));

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            movingCard.setBounds(movingCardX, movingCardY, movingCardWidth, movingCardHeight);

                            movingCard.setIcon(getImageIcon(file, movingCardWidth, movingCardHeight, false));
                            movingCard.repaint();

                        }
                    },
                    1
                    );

            try {
                TimeUnit.MILLISECONDS.sleep(sleepPerFrame);
            } catch (Exception e) {
            }
        }

        // Enables to play
        movingCard.setVisible(false);
        background.remove(movingCard);
    }

    protected void updatePoints(){
        if(finishedGame)
            return;
        int playerPoints = player.getPoints();
        int opponentPoints = opponent.getPoints();

        if(playerPoints > 15)
            playerPoints = 15;
        if(opponentPoints > 15)
            opponentPoints = 15;

        pointsPlayer.setIcon(getImageIcon("src/truco_java/puntaje/" + playerPoints + ".png", 50, 85, false));
        pointsAi.setIcon(getImageIcon("src/truco_java/puntaje/" + opponentPoints + ".png", 50, 85, false));


        if(playerPoints==15)
            actionWhenPlayerWinsGame();
        else if(opponentPoints==15)
            actionWhenOpponentWinsGame();
    }

}
