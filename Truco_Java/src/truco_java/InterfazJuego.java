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

public class InterfazJuego extends JFrame {

    private ArrayList<Carta> mazo = new ArrayList<>();
    private JLabel fondo = new JLabel();
    private Truco_Java menu;
    private JLabel AIC1, AIC2, AIC3;
    private JLabel AICT1, AICT2, AICT3;
    private JButton PC1, PC2, PC3;
    private JLabel PCT1, PCT2, PCT3;
    private JButton truco, envido, irAlMazo, envidoEsp, envidoEnvido, realEnvido, faltaEnvido;
    private JugadorAI ai = new JugadorAI(null, false);
    private Persona jugador = new Persona(null, true);
    private int nivelTruco = 0;
    private ArrayList<Integer> envidosCantados;
    private boolean envidoFinalizado = false;
    private JButton noQuieroEnv, quieroEnv, noQuieroTruco, quieroTruco;
    private JTextPane estado;
    private int habilitadoARetrucar = 0; // 1--> Jugador; 2--> AI
    private JLabel fondoEstado;
    private JLabel puntajeAI = new JLabel(), puntajeJugador = new JLabel();
    private final int numeroPersonaje = new Random().nextInt(6) + 1; // Representa el personaje que fue generado;
    private String nombrePersonaje ="la PC";
    private final Music cantar = new Music();
    private static final Music efectos = new Music();
    private JButton movCarta = new JButton();
    private boolean PC1Enabled=false, PC2Enabled=false, PC3Enabled=false;
    private boolean termino = false;

    public InterfazJuego(Truco_Java menu) throws IOException {
        this.menu = menu;
        cargarMazo();

        setLayout(null);
        setDefaultCloseOperation(3);
        switch(numeroPersonaje){
            case 1: nombrePersonaje="El Carpincho"; break;
            case 2: nombrePersonaje="La Roca"; break;
            case 3: nombrePersonaje="Messi"; break;
            case 4: nombrePersonaje="El Diego"; break;
            case 5: nombrePersonaje="Boris"; break;
            case 6: nombrePersonaje="Guido"; break;
        }

        // Inicializa la carta de movimiento
        movCarta.setOpaque(false);
        movCarta.setContentAreaFilled(false);
        movCarta.setBorderPainted(false);

        // Fondo
        setFondo(0);
        fondo.setBounds(0, 0, 500, 800);
        fondo.setVisible(true);
        add(fondo);

        // AI Carta 1
        AIC1 = new JLabel();
        AIC1.setBounds(100, 70, 75, 100);
        AIC1.setVisible(true);
        fondo.add(AIC1);

        // AI Carta 2
        AIC2 = new JLabel();
        AIC2.setBounds(200, 70, 75, 100);
        AIC2.setVisible(true);
        fondo.add(AIC2);

        // AI Carta 3
        AIC3 = new JLabel();
        AIC3.setBounds(300, 70, 75, 100);
        AIC3.setVisible(true);
        fondo.add(AIC3);

        // Persona Carta 1
        PC1 = new JButton();
        PC1.setBounds(10, 400, 155, 200);
        PC1.setVisible(true);
        PC1Enabled=false;
        PC1.setOpaque(false);
        PC1.setContentAreaFilled(false);
        PC1.setBorderPainted(false);
        fondo.add(PC1);
        PC1.addActionListener((ActionEvent e) -> {
            if(!PC1Enabled)
                return;
            efectos.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efectos.play();
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                tirarCarta(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la primer carta: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            setFondo(0);
        });

        // Persona Carta 2
        PC2 = new JButton();
        PC2.setBounds(170, 400, 155, 200);
        PC2.setVisible(true);
        PC2Enabled=false;
        PC2.setBorderPainted(false);
        PC2.setOpaque(false);
        PC2.setContentAreaFilled(false);
        PC2.setBorderPainted(false);
        fondo.add(PC2);
        PC2.addActionListener((ActionEvent e) -> {
            if(!PC2Enabled)
                return;
            efectos.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efectos.play();
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                tirarCarta(1);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la segunda carta: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            setFondo(0);
        });

        // Persona Carta 3
        PC3 = new JButton();
        PC3.setBounds(330, 400, 155, 200);
        PC3.setVisible(true);
        PC3Enabled=false;
        PC3.setBorderPainted(false);
        PC3.setOpaque(false);
        PC3.setContentAreaFilled(false);
        PC3.setBorderPainted(false);
        fondo.add(PC3);
        PC3.addActionListener((ActionEvent e) -> {
            if(!PC3Enabled)
                return;
            efectos.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efectos.play();
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                tirarCarta(2);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de tirar la tercer carta: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            setFondo(0);
        });

        JButton repartir = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cartasMazo.png")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        repartir.setBounds(365, 280, 80, 80);
        repartir.setBorderPainted(false);
        repartir.setOpaque(false);
        repartir.setContentAreaFilled(false);
        repartir.setBorderPainted(false);
        repartir.setVisible(true);
        fondo.add(repartir);
        repartir.addActionListener((ActionEvent e) -> {
            repartir.setEnabled(false);
            try {
                otraPartida();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de comenzar otra partida: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // AI Carta Tirada 1
        AICT1 = new JLabel();
        AICT1.setBounds(130, 210, 70, 80);
        AICT1.setVisible(true);
        fondo.add(AICT1);

        // AI Carta Tirada 2
        AICT2 = new JLabel();
        AICT2.setBounds(210, 210, 70, 80);
        AICT2.setVisible(true);
        fondo.add(AICT2);

        // AI Carta Tirada 3
        AICT3 = new JLabel();
        AICT3.setBounds(290, 210, 70, 80);
        AICT3.setVisible(true);
        fondo.add(AICT3);

        // Persona Carta Tirada 1
        PCT1 = new JLabel();
        PCT1.setBounds(130, 310, 70, 80);
        PCT1.setVisible(true);
        fondo.add(PCT1);

        // Persona Carta Tirada 2
        PCT2 = new JLabel();
        PCT2.setBounds(210, 310, 70, 80);
        PCT2.setVisible(true);
        fondo.add(PCT2);

        // Persona Carta Tirada 3
        PCT3 = new JLabel();
        PCT3.setBounds(290, 310, 70, 80);
        PCT3.setVisible(true);
        fondo.add(PCT3);

        // Fondo de la respuesta de la AI
        fondoEstado = new JLabel(new ImageIcon("src/truco_java/fondos/burbuja.png"));
        fondoEstado.setBounds(50,170,400,45);
        fondoEstado.setOpaque(false);
        fondoEstado.setVisible(false);
        fondo.add(fondoEstado);

        estado = new JTextPane();
        estado.setBounds(0,0,400,45);
        estado.setFont(new Font("Serif", Font.ITALIC, 30));
        estado.setEditable(false);
        estado.setOpaque(false);
        estado.setVisible(true);
        fondoEstado.add(estado);
        //Centra el texto
        StyledDocument doc = estado.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Boton envido general
        envido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        envido.setBounds(170, 660, 155, 60);
        envido.setVisible(true);
        envido.setEnabled(false);
        envido.setOpaque(false);
        envido.setContentAreaFilled(false);
        envido.setBorderPainted(false);
        fondo.add(envido);
        envido.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            if (envidoEsp.isVisible() == true) {
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
            } else {
                envidoEsp.setVisible(true);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
            }
        });

        // Boton Ir al mazo
        irAlMazo = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/irAlMazoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        irAlMazo.setBounds(330, 660, 155, 60);
        irAlMazo.setVisible(true);
        irAlMazo.setEnabled(false);
        irAlMazo.setOpaque(false);
        irAlMazo.setContentAreaFilled(false);
        irAlMazo.setBorderPainted(false);
        fondo.add(irAlMazo);
        irAlMazo.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            JOptionPane.showMessageDialog(null, "Te has ido al mazo. Repartiendo...");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            int puntos=0;
            if(!envidoFinalizado && ai.getCartasJugadas().isEmpty())
                puntos++;
            ai.setPuntaje(ai.getPuntaje()+puntos+calcularTrucoGanado(), this);
            try {
                dibujarPuntaje();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar el puntaje: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            try {
                otraPartida();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de comenzar otra partida: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            try {
            habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Boton envido especifico
        envidoEsp = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envidoEsp.setBounds(10, 595, 240, 50);
        envidoEsp.setVisible(false);
        envidoEsp.setOpaque(false);
        envidoEsp.setContentAreaFilled(false);
        envidoEsp.setBorderPainted(false);
        fondo.add(envidoEsp);
        envidoEsp.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            envidosCantados.add(1);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + nombrePersonaje + ": " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Boton envido-envido
        envidoEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/envidoGrandeBoton.png")).getScaledInstance(240, 50, Image.SCALE_SMOOTH)));
        envidoEnvido.setBounds(10, 595, 240, 50);
        envidoEnvido.setVisible(false);
        envidoEnvido.setOpaque(false);
        envidoEnvido.setContentAreaFilled(false);
        envidoEnvido.setBorderPainted(false);
        fondo.add(envidoEnvido);
        envidoEnvido.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            envidosCantados.add(2);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + nombrePersonaje + ": " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Boton Real Envido
        realEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/realEnvidoBoton.png")).getScaledInstance(117, 50, Image.SCALE_SMOOTH)));
        realEnvido.setBounds(252, 595, 117, 50);
        realEnvido.setVisible(false);
        realEnvido.setOpaque(false);
        realEnvido.setContentAreaFilled(false);
        realEnvido.setBorderPainted(false);
        fondo.add(realEnvido);
        realEnvido.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            envidosCantados.add(3);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + nombrePersonaje + ": " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Boton Falta Envido
        faltaEnvido = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/faltaEnvidoBoton.png")).getScaledInstance(117, 50, Image.SCALE_SMOOTH)));
        faltaEnvido.setBounds(373, 595, 117, 50);
        faltaEnvido.setVisible(false);
        faltaEnvido.setOpaque(false);
        faltaEnvido.setContentAreaFilled(false);
        faltaEnvido.setBorderPainted(false);
        fondo.add(faltaEnvido);
        faltaEnvido.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            envidosCantados.add(4);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el envido de " + nombrePersonaje + ": " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        //Botones de Quiero y No quiero envido
        quieroEnv = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/quieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        quieroEnv.setBounds(10, 250, 110, 40);
        quieroEnv.setVisible(false);
        quieroEnv.setOpaque(false);
        quieroEnv.setContentAreaFilled(false);
        quieroEnv.setBorderPainted(false);
        fondo.add(quieroEnv);
        quieroEnv.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            if (jugador.calcularEnvido() > ai.calcularEnvido()) { //Si gana el jugador
                ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                JOptionPane.showMessageDialog(null, "Has ganado. " + nombrePersonaje + " tenía " + ai.calcularEnvido() + " de envido.");
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
            }
            if (jugador.calcularEnvido() < ai.calcularEnvido()) { // Si gana la AI
                ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                JOptionPane.showMessageDialog(null, "Has perdido. " + nombrePersonaje + " tenía " + ai.calcularEnvido() + " de envido.");
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
            }
            if (jugador.calcularEnvido() == ai.calcularEnvido()) { // Si empatan...
                if (jugador.isMano() == true) { // .. y el jugador es mano
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has ganado por mano");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
                } else { // .. y la AI es mano
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has perdido, " + nombrePersonaje + " es mano");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
                }
                try {
                    dibujarPuntaje();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el dibujar el puntaje: " + ex.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
            }
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            PC1Enabled=true;
            PC2Enabled=true;
            PC3Enabled=true;
            try {
                dibujarPuntaje();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el dibujar el puntaje: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            setFondo(0);

            try {
                // Continua con el juego
                habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            });

        noQuieroEnv = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/noQuieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        noQuieroEnv.setBounds(10, 300, 110, 40);
        noQuieroEnv.setVisible(false);
        noQuieroEnv.setOpaque(false);
        noQuieroEnv.setContentAreaFilled(false);
        noQuieroEnv.setBorderPainted(false);
        fondo.add(noQuieroEnv);
        noQuieroEnv.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            ai.setPuntaje(ai.getPuntaje() + calcularEnvidoPerdido(), this);

            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            setFondo(0);
            try {
                dibujarPuntaje();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar los puntajes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }

            try {
                // Continua con el juego
                habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Boton Truco
        truco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setBounds(10, 660, 155, 60);
        truco.setVisible(true);
        truco.setEnabled(false);
        truco.setOpaque(false);
        truco.setContentAreaFilled(false);
        truco.setBorderPainted(false);
        fondo.add(truco);
        truco.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            envidoFinalizado = true;
            if(habilitadoARetrucar != 2){
                nivelTruco++;
                habilitadoARetrucar = 2;
                try {
                    AICantaTruco(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar el truco de " + nombrePersonaje + ": " + ex.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
            }
        });

        //Botones de Quiero y No quiero truco
        quieroTruco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/quieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        quieroTruco.setBounds(10, 250, 110, 40);
        quieroTruco.setVisible(false);
        quieroTruco.setOpaque(false);
        quieroTruco.setContentAreaFilled(false);
        quieroTruco.setBorderPainted(false);
        fondo.add(quieroTruco);
        quieroTruco.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            PC1Enabled=true;
            PC2Enabled=true;
            PC3Enabled=true;

            if(nivelTruco == 3)
                truco.setEnabled(false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            try {
                habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            setFondo(0);
        });


        noQuieroTruco = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/noQuieroBoton.png")).getScaledInstance(110, 40, Image.SCALE_SMOOTH)));
        noQuieroTruco.setBounds(10, 300, 110, 40);
        noQuieroTruco.setVisible(false);
        noQuieroTruco.setOpaque(false);
        noQuieroTruco.setContentAreaFilled(false);
        noQuieroTruco.setBorderPainted(false);
        fondo.add(noQuieroTruco);
        noQuieroTruco.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/boton.wav", 1);
            efectos.play();
            ai.setPuntaje(ai.getPuntaje() + calcularTrucoPerdido(), this);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            try {
                otraPartida();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de comenzar otra partida: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            setFondo(0);
        });

        // Fondo puntaje
        JLabel puntajeFondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ numeroPersonaje +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
        puntajeFondo.setBounds(390, 10, 100, 150);
        puntajeFondo.setVisible(true);
        fondo.add(puntajeFondo);

        // Puntaje Jugador
        puntajeJugador.setBounds(0, 65, 50, 85);
        puntajeJugador.setVisible(true);
        puntajeFondo.add(puntajeJugador);

        // Puntaje Ai
        puntajeAI.setBounds(50, 65, 50, 85);
        puntajeAI.setVisible(true);
        puntajeFondo.add(puntajeAI);

        JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        atras.setOpaque(false);
        atras.setContentAreaFilled(false);
        atras.setBorderPainted(false);
        atras.setBounds(10, 10, 50, 50);
        atras.setVisible(true);
        fondo.add(atras);
        atras.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            // Si todavia no comenzo la partida
            if(repartir.isEnabled()){
                menu.setVisible(true);
                termino=true;
                dispose();
                return;
            }

            // Si se quiere sallir en medio de la partida
            int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida? Se declarará a " + nombrePersonaje + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            if(dialogResult == JOptionPane.YES_OPTION){
                ai.setPuntaje(15, this);
            }
        });
    }

    public void dibujarPuntaje() throws IOException {
        if(termino)
            return;
        int jugadorPunt = jugador.getPuntaje();
        int aiPunt = ai.getPuntaje();
        if(jugadorPunt > 15)
            jugadorPunt = 15;
        if(aiPunt > 15)
            aiPunt = 15;

        puntajeJugador.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + jugadorPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));
        puntajeAI.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + aiPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));

        if(jugadorPunt==15){
            JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó el Jugador. Felicidades");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            menu.setVisible(true);
            termino=true;
            otraPartida();
            if(!menu.facil.isSelected()) {
                menu.ganadasJugador++;
                menu.puntajeJugador.setText(Integer.toString(menu.ganadasJugador));
                menu.puntajeFondo.setVisible(true);
            }
            dispose();
        }
        if(aiPunt==15){
            JOptionPane.showMessageDialog(null, "Termino el Juego. Ganó " + nombrePersonaje + ". Será la próxima...");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            menu.setVisible(true);
            termino=true;
            otraPartida();
            if(!menu.facil.isSelected()) {
                menu.ganadasAI++;
                menu.puntajeAI.setText(Integer.toString(menu.ganadasAI));
                menu.puntajeFondo.setVisible(true);
            }
            dispose();
        }
    }

    private void cargarMazo() {
        mazo.add(new Carta(1, "espada"));
        mazo.add(new Carta(2, "espada"));
        mazo.add(new Carta(3, "espada"));
        mazo.add(new Carta(4, "espada"));
        mazo.add(new Carta(5, "espada"));
        mazo.add(new Carta(6, "espada"));
        mazo.add(new Carta(7, "espada"));
        mazo.add(new Carta(10, "espada"));
        mazo.add(new Carta(11, "espada"));
        mazo.add(new Carta(12, "espada"));
        mazo.add(new Carta(1, "basto"));
        mazo.add(new Carta(2, "basto"));
        mazo.add(new Carta(3, "basto"));
        mazo.add(new Carta(4, "basto"));
        mazo.add(new Carta(5, "basto"));
        mazo.add(new Carta(6, "basto"));
        mazo.add(new Carta(7, "basto"));
        mazo.add(new Carta(10, "basto"));
        mazo.add(new Carta(11, "basto"));
        mazo.add(new Carta(12, "basto"));
        mazo.add(new Carta(1, "oro"));
        mazo.add(new Carta(2, "oro"));
        mazo.add(new Carta(3, "oro"));
        mazo.add(new Carta(4, "oro"));
        mazo.add(new Carta(5, "oro"));
        mazo.add(new Carta(6, "oro"));
        mazo.add(new Carta(7, "oro"));
        mazo.add(new Carta(10, "oro"));
        mazo.add(new Carta(11, "oro"));
        mazo.add(new Carta(12, "oro"));
        mazo.add(new Carta(1, "copa"));
        mazo.add(new Carta(2, "copa"));
        mazo.add(new Carta(3, "copa"));
        mazo.add(new Carta(4, "copa"));
        mazo.add(new Carta(5, "copa"));
        mazo.add(new Carta(6, "copa"));
        mazo.add(new Carta(7, "copa"));
        mazo.add(new Carta(10, "copa"));
        mazo.add(new Carta(11, "copa"));
        mazo.add(new Carta(12, "copa"));
    }

    private void mezclarMazo() {
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            ArrayList<Carta> mazoTemp = new ArrayList<>();
            for (int x = 0; x < mazo.size() + mazoTemp.size(); x++) {
                int posMezcla = random.nextInt(mazo.size());
                mazoTemp.add(mazo.get(posMezcla));
                mazo.remove(posMezcla);
            }
            mazo.clear();
            mazo.addAll(mazoTemp);
        }
    }

    private void dibujarCartas() throws IOException {
        AIC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        AIC2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        AIC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));

        switch (ai.getMano().size()) {
            case 0:
                AIC1.setVisible(false);
                AIC2.setVisible(false);
                AIC3.setVisible(false);
                break;
            case 1:
                AIC1.setVisible(true);
                AIC2.setVisible(false);
                AIC3.setVisible(false);
                break;
            case 2:
                AIC1.setVisible(true);
                AIC2.setVisible(true);
                AIC3.setVisible(false);
                break;
            case 3:
                AIC1.setVisible(true);
                AIC2.setVisible(true);
                AIC3.setVisible(true);
                break;
        }

        switch (jugador.getMano().size()) {
            case 0:
                PC1.setVisible(false);
                PC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                PC2.setVisible(false);
                PC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                PC3.setVisible(false);
                PC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                break;
            case 1:
                PC1.setVisible(true);
                PC1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(0).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                PC2.setVisible(false);
                PC2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                PC3.setVisible(false);
                PC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                break;
            case 2:
                PC1.setVisible(true);
                PC1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(0).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                PC2.setVisible(true);
                PC2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(1).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                PC3.setVisible(false);
                PC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                break;
            case 3:
                PC1.setVisible(true);
                PC1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(0).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                //PC1.setIcon(new ImageIcon(p.getMano().get(0).linkCarta()));
                PC2.setVisible(true);
                PC2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(1).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                //PC2.setIcon(new ImageIcon(p.getMano().get(1).linkCarta()));
                PC3.setVisible(true);
                PC3.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(2).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
                //PC3.setIcon(new ImageIcon(p.getMano().get(2).linkCarta()));
                break;
        }

        // Cartas ya tiradas
        switch (ai.getCartasJugadas().size()) {
            case 3:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT3.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(2).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT3.setIcon(null);
                break;
            case 1:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(null);
                AICT3.setIcon(null);
                break;
            case 0:
                AICT1.setIcon(null);
                AICT2.setIcon(null);
                AICT3.setIcon(null);
                break;
        }

        switch (jugador.getCartasJugadas().size()) {
            case 3:
                PCT1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT3.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(2).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                PCT1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT3.setIcon(null);
                break;
            case 1:
                PCT1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT2.setIcon(null);
                PCT3.setIcon(null);
                break;
            case 0:
                PCT1.setIcon(null);
                PCT2.setIcon(null);
                PCT3.setIcon(null);
                break;
        }
    }

    private void dibujarBotones() throws IOException {
        switch (nivelTruco) {
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

    private void tirarCarta(int pos) throws IOException {
        // Intenta solucionar un bug de diferentes llamados que pueden perjudicar a la mano
        PC1Enabled=false;
        PC2Enabled=false;
        PC3Enabled=false;

        if(!menu.movCartas.isSelected()) moverCartaPersona(pos, jugador.getCartasJugadas().size());

        jugador.agregarCartaJugada(pos);

        if(menu.movCartas.isSelected()){ //Si no se quiere movimiento de cartas
            try {
                dibujarCartas();
                System.out.println("medio: " + envido.isEnabled());
                habilitaTurno();
                System.out.println("despues: " + envido.isEnabled());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        }
    }

    private void otraPartida() throws IOException {
        if(!termino && menu.musica.isSelected() && !menu.movCartas.isSelected()) {
            efectos.setFile("src/truco_java/musica/otraPartida.wav", 1);
            efectos.play();
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Reinicia variables
        nivelTruco = 0;
        envidosCantados = new ArrayList<>();
        envidoFinalizado = false;
        habilitadoARetrucar = 0;
        dibujarBotones();
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setVisible(true);
        setFondo(0);
        ai.setEnvidoJugadorCantado(-1);

        // Limpia las manos
        jugador.setMano(new ArrayList<>());
        ai.setMano(new ArrayList<>());
        jugador.setCartasJugadas(new ArrayList<>());
        ai.setCartasJugadas(new ArrayList<>());

        mezclarMazo();

        // Oculta y resetea botones
        quieroEnv.setVisible(false);
        noQuieroEnv.setVisible(false);
        envido.setEnabled(false);
        envidoEsp.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        imprimeAIEnvido(0, false);
        truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/trucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
        truco.setEnabled(true);

        //Reparte
        ArrayList<Carta> mano1 = new ArrayList<>();
        mano1.add(mazo.get(0));
        mano1.add(mazo.get(2));
        mano1.add(mazo.get(4));
        ArrayList<Carta> mano2 = new ArrayList<>();
        mano2.add(mazo.get(1));
        mano2.add(mazo.get(3));
        mano2.add(mazo.get(5));

        if (ai.isMano() == true) {
            ai.setMano(mano1);
            jugador.setMano(mano2);
            ai.setEsMano(false);
            jugador.setEsMano(true);
        } else {
            ai.setMano(mano2);
            jugador.setMano(mano1);
            ai.setEsMano(true);
            jugador.setEsMano(false);
        }

        // Aca no tiene que ir un habilitaTurno() porque sino tira dos veces  la AI
        // Muestra puntaje
        dibujarPuntaje();
        dibujarCartas();

    }

    private void habilitaTurno() throws IOException {
        if(termino)
            return;
        if(compruebaSiTerminoPartida()==1) {
            JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó el Jugador.");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            // Suma puntos al ganador
            jugador.setPuntaje(jugador.getPuntaje() + calcularTrucoGanado(), this);
            otraPartida();
            habilitaTurno();
            return;
        }
        if(compruebaSiTerminoPartida()==2) {
            JOptionPane.showMessageDialog(null, "Termino la Partida. Ganó " + nombrePersonaje + ".");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            // Suma puntos al ganador
            ai.setPuntaje(ai.getPuntaje() + calcularTrucoGanado(), this);
            otraPartida();
            habilitaTurno();
            return;
        }

        if (jugador.getCartasJugadas().isEmpty() && ai.getCartasJugadas().isEmpty()) { // No jugo nadie
            if (jugador.isMano() == true) {
                if(habilitadoARetrucar < 2) truco.setEnabled(true);
                if(!envidoFinalizado) envido.setEnabled(true);
                irAlMazo.setEnabled(true);
                PC1Enabled=true;
                PC2Enabled=true;
                PC3Enabled=true;
            } else {
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
                // No hace falta que se cante envido, porque ya lo verifica dentro de AICantaTruco
                if (AICantaTruco(false)==0);
                else return;
                ai.jugarTurno(jugador, this);
                moverCartaAI(ai.getMano().size(), ai.getCartasJugadas().size()-1);
            }
        } else if (jugador.getCartasJugadas().isEmpty() && !ai.getCartasJugadas().isEmpty()) { // Ya Jugó la AI. Turno Jugador
            if(habilitadoARetrucar < 2) truco.setEnabled(true);
            if(!envidoFinalizado) envido.setEnabled(true);
            irAlMazo.setEnabled(true);
            PC1Enabled=true;
            PC2Enabled=true;
            PC3Enabled=true;
        } else if (!jugador.getCartasJugadas().isEmpty() && ai.getCartasJugadas().isEmpty()) { // Ya Jugó el Jugador. Turno AI
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
                // No hace falta que se cante envido, porque ya lo verifica dentro de AICantaTruco
                if (AICantaTruco(false)==0);
                else return;
                ai.jugarTurno(jugador, this);
                moverCartaAI(ai.getMano().size(), ai.getCartasJugadas().size()-1);
        } else if (!jugador.getCartasJugadas().isEmpty() && !ai.getCartasJugadas().isEmpty()) { // Rondas 2 y 3
            if (jugador.getCartasJugadas().size() == ai.getCartasJugadas().size()) { // Si es una ronda en la que nadie jugó
                int rankingJugador = jugador.getCartasJugadas().get(jugador.getCartasJugadas().size()-1).rankingCarta();
                int rankingAI = ai.getCartasJugadas().get(ai.getCartasJugadas().size()-1).rankingCarta();
                envido.setEnabled(false); // Deshabilita el envido en la segunda ronda

                if (rankingJugador > rankingAI) { // Si gano jugador en la anterior ronda
                    if(habilitadoARetrucar < 2) truco.setEnabled(true); // Si le corresponde retrucar
                    // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
                    if(ai.getCartasJugadas().size() == 3) if(ai.getCartasJugadas().get(2).rankingCarta()==0) truco.setEnabled(false);
                    irAlMazo.setEnabled(true);
                    PC1Enabled=true;
                    PC2Enabled=true;
                    PC3Enabled=true;
                } else if (rankingAI > rankingJugador) { // si gano AI en la anterior ronda
                    truco.setEnabled(false);
                    envido.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    PC1Enabled=false;
                    PC2Enabled=false;
                    PC3Enabled=false;
                    if (AICantaTruco(false)==0);
                    else return;
                    ai.jugarTurno(jugador, this);
                    moverCartaAI(ai.getMano().size(), ai.getCartasJugadas().size()-1);
                } else if(rankingJugador == rankingAI){ // Si empatan
                    if(jugador.isMano()){ // Es mano el jugador
                        if(habilitadoARetrucar < 2) truco.setEnabled(true);
                        // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
                        if(ai.getCartasJugadas().size() == 3) if(ai.getCartasJugadas().get(2).rankingCarta()==0) truco.setEnabled(false);
                        irAlMazo.setEnabled(true);
                        PC1Enabled=true;
                        PC2Enabled=true;
                        PC3Enabled=true;
                    } else { // Es mano la AI
                        truco.setEnabled(false);
                        envido.setEnabled(false);
                        irAlMazo.setEnabled(false);
                        PC1Enabled=false;
                        PC2Enabled=false;
                        PC3Enabled=false;
                        if (AICantaTruco(false)==0);
                        else return;
                        ai.jugarTurno(jugador, this);
                        moverCartaAI(ai.getMano().size(), ai.getCartasJugadas().size()-1);
                    }
                }
            } else if (jugador.getCartasJugadas().size() == ai.getCartasJugadas().size() - 1) { // Si ya la AI tiró en esa ronda
                if(habilitadoARetrucar < 2) truco.setEnabled(true);
                // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
                if(ai.getCartasJugadas().size() == 3) if(ai.getCartasJugadas().get(2).rankingCarta()==0) truco.setEnabled(false);
                irAlMazo.setEnabled(true);
                PC1Enabled=true;
                PC2Enabled=true;
                PC3Enabled=true;
            } else if (jugador.getCartasJugadas().size() - 1 == ai.getCartasJugadas().size()) { // Si ya el jugador tiró en esa ronda
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
                if (AICantaTruco(false)==0);
                else return;
                ai.jugarTurno(jugador, this);
                moverCartaAI(ai.getMano().size(), ai.getCartasJugadas().size()-1);
            }
        }
    }

    public int AICantaEnvido() throws IOException {
        if (envidoFinalizado == true)
            return 0;

        int desicion;
        if (envidosCantados.isEmpty())
            desicion = ai.desidirEnvido(0, jugador, menu);

        else {
            desicion = ai.desidirEnvido(envidosCantados.get(envidosCantados.size() - 1), jugador, menu);

            if (desicion == envidosCantados.get(envidosCantados.size() - 1)) { // Si la AI Acepta lo cantado
                cantar.setFile("src/truco_java/cantos/envido/" + numeroPersonaje + "5.wav", 1);
                cantar.play();

                if (jugador.calcularEnvido() > ai.calcularEnvido()) { //Si gana el jugador
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Has ganado. " + nombrePersonaje + " tenía " + ai.calcularEnvido() + " de envido.");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
                }
                if (jugador.calcularEnvido() < ai.calcularEnvido()) { // Si gana la AI
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Has perdido. " + nombrePersonaje + " tenía " + ai.calcularEnvido() + " de envido.");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
                }
                if (jugador.calcularEnvido() == ai.calcularEnvido()) { // Si empatan...
                    if (jugador.isMano() == true) { // .. y el jugador es mano
                        ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has ganado por mano");
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                        jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
                    } else { // .. y la AI es mano
                        ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has perdido, " + nombrePersonaje + " es mano");
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                        ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
                    }
                }
                quieroEnv.setVisible(false);
                noQuieroEnv.setVisible(false);
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
                PC1Enabled=true;
                PC2Enabled=true;
                PC3Enabled=true;
                if(envidoFinalizado==true) habilitaTurno();
                dibujarPuntaje();
                return 1;
            }
        }

        switch (desicion) {
            case 0:
                if(!envidosCantados.isEmpty()){
                    imprimeAIEnvido(-1, false);
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoPerdido(), this);
                    envidoFinalizado=true;
                    envido.setEnabled(false);
                    envidoEsp.setVisible(false);
                    envidoEnvido.setVisible(false);
                    realEnvido.setVisible(false);
                    faltaEnvido.setVisible(false);
                    quieroEnv.setVisible(false);
                    noQuieroEnv.setVisible(false);
                    habilitaTurno();
                }
                return 0;
            case 1:
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(true);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
                break;
            case 2:
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
                break;
            case 3:
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(true);
                break;
            case 4:
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                break;
        }
        // Agrega el envido que eligio la AI a la lista y lo imprime
        envidosCantados.add(desicion);
        imprimeAIEnvido(desicion, false);

        // Boton Quiero Envido
        quieroEnv.setVisible(true);

        // Boton No Quiero Envido
        noQuieroEnv.setVisible(true);

        // No puede tirar cartas mientras este en envido
        PC1Enabled=false;
        PC2Enabled=false;
        PC3Enabled=false;

        return 1;
    }

    private int calcularEnvidoGanado(int puntajePerdedor) {
        int total = 0;

        for (int i = 0; i < envidosCantados.size(); i++) {
            switch (envidosCantados.get(i)) {
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
                    total = 15 - puntajePerdedor;
            }
        }

        envidoFinalizado = true;
        return total;
    }

    private int calcularEnvidoPerdido() {
        int total = 0;

        for (int i = 0; i < envidosCantados.size()-1; i++) {
            switch (envidosCantados.get(i)) {
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

        if(envidosCantados.size() == 1) // Si solo se canto un envido, da un punto
            total+=1;

        envidoFinalizado = true;
        return total;
    }

    private void imprimeAIEnvido(int envido, boolean esLlamadoDesdeTimer){
        String texto = " ";
        fondoEstado.setVisible(true);
        estado.setFont(new Font("Serif", Font.ITALIC, 30));
        estado.setVisible(true);

        if(!esLlamadoDesdeTimer && envido!=0) {
            cantar.setFile("src/truco_java/cantos/envido/" + numeroPersonaje + envido + ".wav", 1);
            cantar.play();
        }

        switch(envido){
            case -1:
                texto = "No quiero!";
                if(numeroPersonaje==5) texto = "No tea? so No quiero!";
                break;
            case 0:
                fondoEstado.setVisible(false);
                estado.setText("");
                setFondo(0);
                return;
            case 1:
                texto = "Envido!";
                if(numeroPersonaje==5) texto = "Why not? Envido!";
                break;
            case 2:
                texto = "Envido!";
                if(numeroPersonaje==5) {
                    texto = "What about another Envido?!";
                    estado.setFont(new Font("Serif", Font.ITALIC, 25));
                }
                break;
            case 3:
                texto = "Real Envido!";
                if(numeroPersonaje==5) texto = "I say Real Envido!";
                break;
            case 4:
                texto = "Falta Envido!";
                if(numeroPersonaje==5) texto = "Falta Envido my mate!";
                break;
        }

        estado.setText(texto);
        setFondo(1);
        final String textoTimer = texto; // tengo que crear una variable final porque sino el timer no me la acepta

        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    if(estado.getText().equals(textoTimer)){ // Si sigue siendo el mismo texto que se puso antes y nada lo cambio (por ejemplo retrucar el envido o un truco
                        if(quieroEnv.isVisible()) // Si todavia el usuario no se decidió
                            imprimeAIEnvido(envido, true); // Lo vuelve a imprimir
                        else imprimeAIEnvido(0, true); // sino lo desaparece
                    }
                }
            },
            2000
            );
    }

    private int compruebaSiTerminoPartida(){
        if(jugador.getCartasJugadas().size() != ai.getCartasJugadas().size() || ai.getCartasJugadas().isEmpty())
            return 0;

        int ganoJugador=0, ganoAI=0, ganador=0;
        boolean empateDefine=false;

        for(int i=0;i<jugador.getCartasJugadas().size();i++){ // se fija por cada ronda
            if(ganador!=0)
                break;

            OUTER:
            OUTER_1:
            switch (i) {
                case 0:
                    switch (ganaRonda(i)) {
                        case 1:
                            ganoJugador++;
                            break;
                        case 2:
                            ganoAI++;
                            break;
                        case 0:
                            empateDefine=true;
                            break;
                        default:
                            break;
                    }
                    break;
                case 1:
                    switch (ganaRonda(i)) {
                        case 1:
                            if (empateDefine==true) {
                                ganador=1;
                                break OUTER_1;
                            } else {
                                ganoJugador++;
                            }
                            break;
                        case 2:
                            if (empateDefine==true) {
                                ganador=2;
                                break OUTER_1;
                            } else {
                                ganoAI++;
                            }
                            break;
                        case 0:
                            switch(ganaRonda(0)){
                                case 0:
                                    empateDefine=true;
                                    break;
                                case 1:
                                    ganador=1;
                                    break;
                                case 2:
                                    ganador=2;
                                    break;
                            }   break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (ganaRonda(i)) {
                        case 1:
                            if (empateDefine==true) {
                                ganador=1;
                                break OUTER;
                            } else {
                                ganoJugador++;
                            }
                            break;
                        case 2:
                            if (empateDefine==true) {
                                ganador=2;
                                break OUTER;
                            } else {
                                ganoAI++;
                            }
                            break;
                        case 0:
                            switch(ganaRonda(0)){
                                case 0:
                                    empateDefine=true; break;
                                case 1:
                                    ganador=1;
                                    break;
                                case 2:
                                    ganador=2;
                                    break;
                            }   break;
                        default:
                            break;
                    }
                    break;
            }
        }

        // Si empatan las 3 rondas seguidas, gana el que es mano
        if( jugador.getCartasJugadas().size() == 3 && ganaRonda(0)==0 && ganaRonda(1)==0 && ganaRonda(2)==0){
            if(jugador.isMano())
                return 1;
            else
                return 2;
        }


        if(empateDefine==false && (ganoJugador>=2 || ganoAI>=2)){
            if(ganoJugador>ganoAI)
                ganador=1;
            else
                ganador=2;
        }

        return ganador;
    }


    private int ganaRonda(int pos){
        int rankingJugador = jugador.getCartasJugadas().get(pos).rankingCarta(); // carta del jugador
        int rankingAI = ai.getCartasJugadas().get(pos).rankingCarta(); // carta de la AI

        if(rankingJugador > rankingAI) // si gana jugador
            return 1;
        if(rankingJugador < rankingAI) // si gana AI
            return 2;
        // Si empata
        return 0;
    }

    // Responder es un booleano que indica que si se le canto truco. Sirve para saber si puede rechazar a lo que el usuario le cantó
    public int AICantaTruco(boolean responder) throws IOException { // El return 0 significa no hacer nada y el return 1 significa que frena la ejecucion en busca de contestacion del usuario
        if (habilitadoARetrucar == 1)
            return 0;

        // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
        if(ai.getCartasJugadas().size() == 3){
            if(ai.getCartasJugadas().get(2).rankingCarta()==0) {
                truco.setEnabled(false);
                return 0;
            }
        }
        if(jugador.getCartasJugadas().size() == 3) {
            if(jugador.getCartasJugadas().get(2).rankingCarta()==0)
                return 0;
        }

        // Si no se canto envido y es la primer ronda
        if(!envidoFinalizado && ai.getCartasJugadas().isEmpty()){
            if(AICantaEnvido()!=0){ // Se pregunta si quiere cantar envido
                if(nivelTruco!=0) nivelTruco--;
                habilitadoARetrucar = 0;
                return 1;
            }
        }

        int desicion = ai.desidirTruco(nivelTruco, jugador, menu);

        if(desicion == nivelTruco && nivelTruco==0) // Si no se canto nada y no quiere truco
            return 0;

        if(desicion==0){ // si no quiere truco la AI
            if (habilitadoARetrucar == 2 && responder) { // SI tiene que contestar
                imprimeAITruco(-1, false);
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                jugador.setPuntaje(jugador.getPuntaje() + calcularTrucoPerdido(), this);
                JOptionPane.showMessageDialog(null, "" + nombrePersonaje + " ha rechazado el Truco. Repartiendo...");
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                otraPartida();
                habilitaTurno();
                return 1;
            } else return 0;
        }

        PC1Enabled=false;
        PC2Enabled=false;
        PC3Enabled=false;

        if(desicion == nivelTruco && responder){ // Si acepta el truco
            habilitadoARetrucar=2;
            truco.setEnabled(false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            // Al aceptar truco se deshabilita el envido
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            // Habilita las cartas
            PC1Enabled=true;
            PC2Enabled=true;
            PC3Enabled=true;
            imprimeAITruco(4, false);

            habilitaTurno();
            return 0;
        }

        if(!responder && desicion<(nivelTruco+1)) // Si no estoy respondiendo y quiero menos de la oferta actual (lo cual no se puede), no hacer nada
            return 0;
        if(responder && desicion<(nivelTruco+1)){ // Si estoy respondiendo y quiero menos de la oferta actual (lo cual no se puede), imprime no quiero
            imprimeAITruco(-1, false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            jugador.setPuntaje(jugador.getPuntaje() + calcularTrucoPerdido(), this);
            JOptionPane.showMessageDialog(null, "" + nombrePersonaje + " ha rechazado el Truco. Repartiendo...");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            otraPartida();
            habilitaTurno();
            return 1;
        }

        // Si retruca, imprime el mensaje y le pasa el mando al jugador a aceptar o aumentar la apuesta
        habilitadoARetrucar=1;
        // if(desicion>nivelTruco)
        nivelTruco++;
        imprimeAITruco(nivelTruco, false);
        quieroTruco.setVisible(true);
        noQuieroTruco.setVisible(true);

        // Al cantar truco se deshabilita el truco
        envido.setEnabled(false);
        envidoEsp.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);

        return 1;
    }

    private void imprimeAITruco(int trucoMSG, boolean esLlamadoDesdeTimer) throws IOException{
        setFondo(1);
        fondoEstado.setVisible(true);

        if(!esLlamadoDesdeTimer && trucoMSG!=0) {
            cantar.setFile("src/truco_java/cantos/truco/" + numeroPersonaje + trucoMSG + ".wav", 1);
            cantar.play();
        }

        switch(trucoMSG){
            case -1:
                estado.setText("No quiero!");
                if(numeroPersonaje==5) estado.setText("This’s a rip-off. No quiero");
                break;
            case 0:
                fondoEstado.setVisible(false);
                estado.setText("");
                setFondo(0);
                return;
            case 1:
                estado.setText("Truco!");
                if(numeroPersonaje==5) estado.setText("Come on, Truco!");
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/retrucoBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                truco.setEnabled(true);
                break;
            case 2:
                estado.setText("Re truco!");
                if(numeroPersonaje==5) estado.setText("Re truco if you're brave!");
                truco.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/valeCuatroBoton.png")).getScaledInstance(155, 60, Image.SCALE_SMOOTH)));
                truco.setEnabled(true);
                break;
            case 3:
                estado.setText("Quiero vale 4!");
                if(numeroPersonaje==5) estado.setText("Really? Quiero vale cuatro");
                truco.setVisible(false);
                break;
            case 4:
                estado.setText("Quiero!");
                if(numeroPersonaje==5) estado.setText("Easy peasy. Quiero!");
                truco.setEnabled(false);
                // setFondo(0);
                break;
        }

        envidoFinalizado = true;
        final String textoTimer = estado.getText();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if(estado.getText().equals(textoTimer)){
                            if(quieroTruco.isVisible()){
                                try {
                                    imprimeAITruco(trucoMSG, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + nombrePersonaje + " en el truco: " + ex.getMessage());
                                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    efectos.play();
                                }
                            }
                            else {
                                try {
                                    imprimeAITruco(0, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + nombrePersonaje + " en el truco: " + ex.getMessage());
                                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    efectos.play();
                                }
                            }
                        }
                    }
                },
                2000
        );
    }

    private int calcularTrucoGanado() {
        switch(nivelTruco){
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

        switch(nivelTruco-1){
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

    private void setFondo(int estadoPers){
        char estadoPersChar;
        if(estadoPers==0) // Fondo persoonaje normal
            estadoPersChar = 'a'; // Personaje AI normal
        else
            estadoPersChar = 'b'; // personaje AI Pregunta (truco, envido, o retrucar cualquiera de las anteriores)

        String imagen = "src/truco_java/fondos/bg" + numeroPersonaje + estadoPersChar + ".png";
        fondo.setIcon(new ImageIcon(imagen));
    }

    private void moverCartaAI(int origen, int destino) throws IOException{
        final int origenX, destinoX;
        final String archivo = ai.getCartasJugadas().get(ai.getCartasJugadas().size()-1).linkCarta();

        //Según qué carta sea, la oculta y pone una temporal en reemplazo
        // PostData: tengo que agarrar la ultima carta jugada para obtener su imagen
        //  porque antes de llamar a moverCartaAI, tiro la carta desde la clase jugador
        switch(origen){
            case 0:
                AIC1.setVisible(false);
                origenX=100;
                break;
            case 1:
                AIC2.setVisible(false);
                origenX=180;
                break;
            case 2:
                AIC3.setVisible(false);
                origenX=280;
                break;
            default:
                origenX=0;
                break;
        }
        switch(destino){
            case 0:
                destinoX=130;
                break;
            case 1:
                destinoX=220;
                break;
            case 2:
                destinoX=320;
                break;
            default:
                destinoX=0;
                break;
        }

        if(menu.movCartas.isSelected()){ //Si no se quiere movimiento de cartas
            try {
                dibujarCartas();
                habilitaTurno();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            return;
        }

        movCarta.setBounds(origenX, 70, 155, 200);
        movCarta.setVisible(true);
        fondo.add(movCarta);

        int porcentajeMov=40;
        final int movX=(destinoX-origenX)/porcentajeMov, movY=(210-70)/porcentajeMov;
        final int sizeX = (movCarta.getWidth()-70)/porcentajeMov;
        final int sizeY = (movCarta.getHeight()-80)/porcentajeMov;

        Thread thread = new Thread(){
            public void run(){
                timerMovCarta(movX, movY, origenX, 70, destinoX, 210, sizeX, sizeY, archivo);
            }
        };
        thread.start();
    }

    private void moverCartaPersona(int origen, int destino) throws IOException{
        final int origenX, destinoX;
        final String archivo;

        //Según qué carta sea, la oculta y pone una temporal en reemplazo
        switch(origen){
            case 0:
                PC1.setVisible(false);
                archivo = jugador.getMano().get(0).linkCarta();
                origenX=10;
                break;
            case 1:
                PC2.setVisible(false);
                archivo = jugador.getMano().get(1).linkCarta();
                origenX=170;
                break;
            case 2:
                PC2.setVisible(false);
                archivo = jugador.getMano().get(2).linkCarta();
                origenX=330;
                break;
            default:
                origenX=0;
                archivo="";
                break;
        }
        switch(destino){
            case 0:
                destinoX=130;
                break;
            case 1:
                destinoX=210;
                break;
            case 2:
                destinoX=290;
                break;
            default:
                destinoX=0;
                break;
        }

        movCarta.setBounds(origenX, 400, 155, 200);
        movCarta.setVisible(true);
        fondo.add(movCarta);

        int porcentajeMov=40;
        final int movX=(destinoX-origenX)/porcentajeMov, movY=(310-400)/porcentajeMov;
        final int sizeX = (movCarta.getWidth()-70)/porcentajeMov;
        final int sizeY = (movCarta.getHeight()-80)/porcentajeMov;

        Thread thread = new Thread(){
            public void run(){
                timerMovCarta(movX, movY, origenX, 400, destinoX, 310, sizeX, sizeY, archivo);
            }
        };
        thread.start();
    }

  public void timerMovCarta (int movX, int movY, int origenX, int origenY, int destinoX, int destinoY, int ancho, int alto, String archivo) {
      new java.util.Timer().schedule(
            new java.util.TimerTask() {
              @Override
              public void run() {
                  movCarta.setBounds(origenX+movX,origenY+movY, movCarta.getWidth()-ancho, movCarta.getHeight()-alto);
                  try {
                  movCarta.setIcon(new ImageIcon(ImageIO.read(new File(archivo)).getScaledInstance(movCarta.getWidth()-ancho, movCarta.getHeight()-alto, Image.SCALE_SMOOTH)));
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

      movCarta.repaint();
      if(origenX-destinoX>5 || origenX-destinoX<-5){
          timerMovCarta(movX,movY, origenX+movX, origenY+movY, destinoX, destinoY, ancho, alto, archivo);
          return;
      }

      try {
          TimeUnit.MILLISECONDS.sleep(20);
      } catch (Exception e) {
      }


      // Permite jugar
      movCarta.setVisible(false);
      fondo.remove(movCarta);
      try {
          dibujarCartas();
      } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar las cartas: " + ex.getMessage());
          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
          efectos.play();
      }
      try {
          habilitaTurno();
      } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de habilitar los turnos: " + ex.getMessage());
          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
          efectos.play();
      }
  }
}
