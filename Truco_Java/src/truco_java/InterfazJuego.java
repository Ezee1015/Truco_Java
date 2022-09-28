package truco_java;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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

    private static ArrayList<Carta> mazo = new ArrayList<>();
    JLabel fondo = new JLabel();
    JLabel AIC1, AIC2, AIC3;
    JLabel AICT1, AICT2, AICT3;
    JButton PC1, PC2, PC3;
    JLabel PCT1, PCT2, PCT3;
    JButton truco, envido, irAlMazo, envidoEsp, envidoEnvido, realEnvido, faltaEnvido;
    JugadorAI ai = new JugadorAI(null, false);
    Persona jugador = new Persona(null, true);
    int nivelTruco = 0;
    ArrayList<Integer> envidosCantados;
    boolean envidoFinalizado = false;
    JButton noQuieroEnv, quieroEnv, noQuieroTruco, quieroTruco;
    JTextPane estado;
    JLabel puntajeNumero;
    JTextPane puntaje;
    int habilitadoARetrucar = 0; // 1--> Jugador; 2--> AI
    JLabel fondoEstado;
    JLabel puntajeAI = new JLabel(), puntajeJugador = new JLabel();
    private final int numeroPersonaje = new Random().nextInt(6) + 1; // Este numero representa el personaje que fue generado;

    public InterfazJuego() throws IOException {
        cargarMazo();

        setLayout(null);
        setDefaultCloseOperation(3);

        // Fondo
        setFondo(0);
        fondo.setBounds(0, 0, 500, 800);
        fondo.setVisible(true);
        add(fondo);

        // Mezclar el fondo y el personaje y hacer diferetes fondos. Luego se eligen aleatorios.
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
        PC1.setOpaque(false);
        PC1.setContentAreaFilled(false);
        PC1.setBorderPainted(false);
        PC1.setBorderPainted(false);
        fondo.add(PC1);
        PC1.addActionListener((ActionEvent e) -> {
            try {
                tirarCarta(0);
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            setFondo(0);
        });

        // Persona Carta 2
        PC2 = new JButton();
        PC2.setBounds(170, 400, 155, 200);
        PC2.setVisible(true);
        PC2.setBorderPainted(false);
        PC2.setOpaque(false);
        PC2.setContentAreaFilled(false);
        PC2.setBorderPainted(false);
        fondo.add(PC2);
        PC2.addActionListener((ActionEvent e) -> {
            try {
                tirarCarta(1);
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Persona Carta 3
        PC3 = new JButton();
        PC3.setBounds(330, 400, 155, 200);
        PC3.setVisible(true);
        PC3.setBorderPainted(false);
        PC3.setOpaque(false);
        PC3.setContentAreaFilled(false);
        PC3.setBorderPainted(false);
        fondo.add(PC3);
        PC3.addActionListener((ActionEvent e) -> {
            try {
                tirarCarta(2);
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
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
        envido = new JButton("Envido");
        envido.setBounds(170, 660, 155, 60);
        envido.setVisible(true);
        envido.setEnabled(false);
        fondo.add(envido);
        envido.addActionListener((ActionEvent e) -> {
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
        irAlMazo = new JButton("Ir al Mazo");
        irAlMazo.setBounds(330, 660, 155, 60);
        irAlMazo.setVisible(true);
        irAlMazo.setEnabled(false);
        fondo.add(irAlMazo);
        irAlMazo.addActionListener((ActionEvent e) -> {
            int puntos=0;
            if(!envidoFinalizado && ai.getCartasJugadas().size()==0)
                puntos++;
            System.out.println("puntos envido: " + puntos + " y truco: " +calcularTrucoGanado() );
            ai.setPuntaje(ai.getPuntaje()+puntos+calcularTrucoGanado(), this);
            try {
                dibujarPuntaje();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                otraPartida();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
            habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Boton envido especifico
        envidoEsp = new JButton("Envido");
        envidoEsp.setBounds(10, 595, 240, 50);
        envidoEsp.setVisible(false);
        fondo.add(envidoEsp);
        envidoEsp.addActionListener((ActionEvent e) -> {
            envidosCantados.add(1);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Boton envido-envido
        envidoEnvido = new JButton("Envido");
        envidoEnvido.setBounds(10, 595, 240, 50);
        envidoEnvido.setVisible(false);
        fondo.add(envidoEnvido);
        envidoEnvido.addActionListener((ActionEvent e) -> {
            envidosCantados.add(2);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Boton Real Envido
        realEnvido = new JButton("Real Envido");
        realEnvido.setBounds(252, 595, 117, 50);
        realEnvido.setVisible(false);
        fondo.add(realEnvido);
        realEnvido.addActionListener((ActionEvent e) -> {
            envidosCantados.add(3);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Boton Falta Envido
        faltaEnvido = new JButton("Falta Envido");
        faltaEnvido.setBounds(373, 595, 117, 50);
        faltaEnvido.setVisible(false);
        fondo.add(faltaEnvido);
        faltaEnvido.addActionListener((ActionEvent e) -> {
            envidosCantados.add(4);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //Botones de Quiero y No quiero envido
        quieroEnv = new JButton("Quiero");
        quieroEnv.setBounds(10, 250, 110, 40);
        quieroEnv.setVisible(false);
        fondo.add(quieroEnv);
        quieroEnv.addActionListener((ActionEvent e) -> {
            if (jugador.calcularEnvido() > ai.calcularEnvido()) { //Si gana el jugador
                ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                JOptionPane.showMessageDialog(null, "Has ganado. La PC tenía " + ai.calcularEnvido() + " de envido.");
                jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
            }
            if (jugador.calcularEnvido() < ai.calcularEnvido()) { // Si gana la AI
                ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                JOptionPane.showMessageDialog(null, "Has perdido. La PC tenía " + ai.calcularEnvido() + " de envido.");
                ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
            }
            if (jugador.calcularEnvido() == ai.calcularEnvido()) { // Si empatan...
                if (jugador.isMano() == true) { // .. y el jugador es mano
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has ganado por mano");
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
                } else { // .. y la AI es mano
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has perdido, la PC es mano");
                    ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
                }
                try {
                    dibujarPuntaje();
                } catch (IOException ex) {
                    Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            PC1.setEnabled(true);
            PC2.setEnabled(true);
            PC3.setEnabled(true);
            try {
            dibujarPuntaje();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            setFondo(0);

            try {
                // Continua con el juego
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            });

        noQuieroEnv = new JButton("No Quiero");
        noQuieroEnv.setBounds(10, 300, 110, 40);
        noQuieroEnv.setVisible(false);
        fondo.add(noQuieroEnv);
        noQuieroEnv.addActionListener((ActionEvent e) -> {
            ai.setPuntaje(ai.getPuntaje() + calcularEnvidoPerdido(jugador.getPuntaje()), this);

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
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                // Continua con el juego
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Boton Truco
        truco = new JButton("Truco");
        truco.setBounds(10, 660, 155, 60);
        truco.setVisible(true);
        truco.setEnabled(false);
        fondo.add(truco);
        truco.addActionListener((ActionEvent e) -> {
            if(habilitadoARetrucar != 2){
                nivelTruco++;
                habilitadoARetrucar = 2;
                try {
                    AICantaTruco(true);
                } catch (IOException ex) {
                    Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // setFondo(0);
        });

        //Botones de Quiero y No quiero truco
        quieroTruco = new JButton("Quiero");
        quieroTruco.setBounds(10, 250, 110, 40);
        quieroTruco.setVisible(false);
        fondo.add(quieroTruco);
        quieroTruco.addActionListener((ActionEvent e) -> {
            if(nivelTruco == 3)
                truco.setEnabled(false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            });


        noQuieroTruco = new JButton("No Quiero");
        noQuieroTruco.setBounds(10, 300, 110, 40);
        noQuieroTruco.setVisible(false);
        fondo.add(noQuieroTruco);
        noQuieroTruco.addActionListener((ActionEvent e) -> {
            ai.setPuntaje(ai.getPuntaje() + calcularTrucoPerdido(), this);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            try {
                otraPartida();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
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

        JButton redibujar = new JButton("info");
        redibujar.setBounds(10, 10, 50, 50);
        redibujar.setVisible(true);
        fondo.add(redibujar);
        redibujar.addActionListener((ActionEvent e) -> {
            info();
        });
    }

    public void dibujarPuntaje() throws IOException {
        int jugadorPunt = jugador.getPuntaje();
        int aiPunt = ai.getPuntaje();
        if(jugadorPunt > 15)
            jugadorPunt = 15;
        if(aiPunt > 15)
            aiPunt = 15;

        puntajeJugador.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + jugadorPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));
        puntajeAI.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + aiPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));

        if(jugadorPunt==15){
            JOptionPane.showMessageDialog(null, "Termino el Juego. Gano el Jugador. Felicidades");
            setVisible(false);
        }
        if(aiPunt==15){
            JOptionPane.showMessageDialog(null, "Termino el Juego. Gano la computadora. Será la próxima...");
            setVisible(false);
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
                int posMezcla = random.nextInt(mazo.size()); // Puede dar numeros negativos !!!!!
                mazoTemp.add(mazo.get(posMezcla));
                mazo.remove(posMezcla);
            }
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

    private void dibujarBotones() {
        switch (nivelTruco) {
            case 0:
                truco.setText("Truco");
                break;
            case 1:
                truco.setText("Retruco");
                break;
            case 2:
                truco.setText("Vale Cuatro");
                break;
        }
    }

    private void tirarCarta(int pos) throws IOException {
        // Intenta solucionar un bug de diferentes llamados que pueden perjudicar a la mano
        PC1.setEnabled(false);
        PC2.setEnabled(false);
        PC3.setEnabled(false);

        jugador.agregarCartaJugada(pos);

        dibujarCartas();
    }

    private void otraPartida() throws IOException {
        // Reinicia variables
        nivelTruco = 0;
        envidosCantados = new ArrayList<>();
        envidoFinalizado = false;
        habilitadoARetrucar = 0;
        dibujarBotones();
        truco.setText("Truco");
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
        truco.setText("Truco");
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
        if(compruebaSiTerminoPartida()==1) {
            JOptionPane.showMessageDialog(null, "Termino Partida. Gano el Jugador.");
            // Suma puntos al ganador
            jugador.setPuntaje(jugador.getPuntaje() + calcularTrucoGanado(), this);
            otraPartida();
            habilitaTurno();
            return;
        }
        if(compruebaSiTerminoPartida()==2) {
            JOptionPane.showMessageDialog(null, "Termino Partida. Gano la PC.");
            // Suma puntos al ganador
            ai.setPuntaje(ai.getPuntaje() + calcularTrucoGanado(), this);
            otraPartida();
            habilitaTurno();
            return;
        }

        if (jugador.getCartasJugadas().isEmpty() && ai.getCartasJugadas().isEmpty()) { // No jugo nadie
            if (jugador.isMano() == true) {
                System.out.println("habilitaTurno(): No jugo nadie, turno Jugador");
                if(habilitadoARetrucar < 2) truco.setEnabled(true);
                envido.setEnabled(true);
                irAlMazo.setEnabled(true);
                PC1.setEnabled(true);
                PC2.setEnabled(true);
                PC3.setEnabled(true);
                info();
            } else {
                System.out.println("habilitaTurno(): No jugo nadie, turno AI");
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1.setEnabled(false);
                PC2.setEnabled(false);
                PC3.setEnabled(false);
                // No hace falta que se cante envido, porque ya lo verifica dentro de AICantaTruco
                if (AICantaTruco(false)==0);
                else return;
                ai.jugarTurno(jugador, this);
                dibujarCartas();
                info();
                habilitaTurno();
            }
        } else if (jugador.getCartasJugadas().isEmpty() && !ai.getCartasJugadas().isEmpty()) { // Ya Jugó la AI. Turno Jugador
            System.out.println("habilitaTurno(): Ya jugó la AI, turno Jugador");
            if(habilitadoARetrucar < 2) truco.setEnabled(true);
            if(!envidoFinalizado) envido.setEnabled(true);
            irAlMazo.setEnabled(true);
            PC1.setEnabled(true);
            PC2.setEnabled(true);
            PC3.setEnabled(true);
            info();
        } else if (!jugador.getCartasJugadas().isEmpty() && ai.getCartasJugadas().isEmpty()) { // Ya Jugó el Jugador. Turno AI
            System.out.println("habilitaTurno(): Ya jugó el Jugador, turno AI");
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1.setEnabled(false);
                PC2.setEnabled(false);
                PC3.setEnabled(false);
                // No hace falta que se cante envido, porque ya lo verifica dentro de AICantaTruco
                if (AICantaTruco(false)==0);
                else return;
                ai.jugarTurno(jugador, this);
                dibujarCartas();
                info();
                habilitaTurno();
        } else if (!jugador.getCartasJugadas().isEmpty() && !ai.getCartasJugadas().isEmpty()) { // Rondas 2 y 3
            if (jugador.getCartasJugadas().size() == ai.getCartasJugadas().size()) { // Si es una ronda en la que nadie jugó
                int rankingJugador = jugador.getCartasJugadas().get(jugador.getCartasJugadas().size()-1).rankingCarta();
                int rankingAI = ai.getCartasJugadas().get(ai.getCartasJugadas().size()-1).rankingCarta();
                envido.setEnabled(false); // Deshabilita el envido en la segunda ronda

                if (rankingJugador > rankingAI) { // Si gano jugador en la anterior ronda
                    System.out.println("habilitaTurno(): Nadie jugó, turno Jugador");
                    if(habilitadoARetrucar < 2) truco.setEnabled(true); // Si le corresponde retrucar
                    // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
                    if(ai.getCartasJugadas().size() == 3) if(ai.getCartasJugadas().get(2).rankingCarta()==0) truco.setEnabled(false);
                    irAlMazo.setEnabled(true);
                    PC1.setEnabled(true);
                    PC2.setEnabled(true);
                    PC3.setEnabled(true);
                    info();
                } else if (rankingAI > rankingJugador) { // si gano AI en la anterior ronda
                    System.out.println("habilitaTurno(): Nadie jugó, turno AI");
                    truco.setEnabled(false);
                    envido.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    PC1.setEnabled(false);
                    PC2.setEnabled(false);
                    PC3.setEnabled(false);
                    if (AICantaTruco(false)==0);
                    else return;
                    ai.jugarTurno(jugador, this);
                    dibujarCartas();
                    info();
                    habilitaTurno();
                } else if(rankingJugador == rankingAI){ // Si empatan
                    if(jugador.isMano()){ // Es mano el jugador
                        System.out.println("habilitaTurno(): Empataron, turno Jugador");
                        if(habilitadoARetrucar < 2) truco.setEnabled(true);
                        // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
                        if(ai.getCartasJugadas().size() == 3) if(ai.getCartasJugadas().get(2).rankingCarta()==0) truco.setEnabled(false);
                        irAlMazo.setEnabled(true);
                        PC1.setEnabled(true);
                        PC2.setEnabled(true);
                        PC3.setEnabled(true);
                        info();
                    } else { // Es mano la AI
                        System.out.println("habilitaTurno(): Empataron, turno AI");
                        truco.setEnabled(false);
                        envido.setEnabled(false);
                        irAlMazo.setEnabled(false);
                        PC1.setEnabled(false);
                        PC2.setEnabled(false);
                        PC3.setEnabled(false);
                        if (AICantaTruco(false)==0);
                        else return;
                        ai.jugarTurno(jugador, this);
                        dibujarCartas();
                        info();
                        habilitaTurno();
                    }
                }
            } else if (jugador.getCartasJugadas().size() == ai.getCartasJugadas().size() - 1) { // Si ya la AI tiró en esa ronda
                System.out.println("habilitaTurno(): Ya jugó AI, turno Jugador");
                if(habilitadoARetrucar < 2) truco.setEnabled(true);
                // Si la ultima carta que le queda al oponente es un 4, no se puede cantar truco
                if(ai.getCartasJugadas().size() == 3) if(ai.getCartasJugadas().get(2).rankingCarta()==0) truco.setEnabled(false);
                irAlMazo.setEnabled(true);
                PC1.setEnabled(true);
                PC2.setEnabled(true);
                PC3.setEnabled(true);
                info();
            } else if (jugador.getCartasJugadas().size() - 1 == ai.getCartasJugadas().size()) { // Si ya el jugador tiró en esa ronda
                System.out.println("habilitaTurno(): Ya jugó Jugador, turno AI");
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1.setEnabled(false);
                PC2.setEnabled(false);
                PC3.setEnabled(false);
                if (AICantaTruco(false)==0);
                else return;
                ai.jugarTurno(jugador, this);
                dibujarCartas();
                info();
                habilitaTurno();
            }
        }
    }

    public int AICantaEnvido() throws IOException {
        if (envidoFinalizado == true)
            return 0;

        System.out.println("llama a AICantaEnvido");

        int desicion;
        if (envidosCantados.isEmpty())
            desicion = ai.desidirEnvido(0);

        else {
            desicion = ai.desidirEnvido(envidosCantados.get(envidosCantados.size() - 1));

            if (desicion == envidosCantados.get(envidosCantados.size() - 1)) { // Si la AI Acepta lo cantado
                Music cantar = new Music();
                if(numeroPersonaje==5) cantar.setFile("src/truco_java/cantos/envido/55.wav", 1);
                else cantar.setFile("src/truco_java/cantos/envido/5.wav", 1);
                cantar.play();

                if (jugador.calcularEnvido() > ai.calcularEnvido()) { //Si gana el jugador
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Has ganado. La PC tenía " + ai.calcularEnvido() + " de envido.");
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
                }
                if (jugador.calcularEnvido() < ai.calcularEnvido()) { // Si gana la AI
                    ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                    JOptionPane.showMessageDialog(null, "Has perdido. La PC tenía " + ai.calcularEnvido() + " de envido.");
                    ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()), this);
                }
                if (jugador.calcularEnvido() == ai.calcularEnvido()) { // Si empatan...
                    if (jugador.isMano() == true) { // .. y el jugador es mano
                        ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has ganado por mano");
                        jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()), this);
                    } else { // .. y la AI es mano
                        ai.setEnvidoJugadorCantado(jugador.calcularEnvido());
                        JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has perdido, la PC es mano");
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
                PC1.setEnabled(true);
                PC2.setEnabled(true);
                PC3.setEnabled(true);
                if(envidoFinalizado==true) habilitaTurno();
                dibujarPuntaje();
                return 1;
            }
        }

        switch (desicion) {
            case 0:
                if(!envidosCantados.isEmpty()){
                    imprimeAIEnvido(-1, false);
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoPerdido(ai.getPuntaje()), this);
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
        PC1.setEnabled(false);
        PC2.setEnabled(false);
        PC3.setEnabled(false);

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
                    total += 15 - puntajePerdedor;
            }
        }

        envidoFinalizado = true;
        return total;
    }

    private int calcularEnvidoPerdido(int puntajePerdedor) {
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
                case 4:
                    System.out.println("error, no debería de haber entrado acá. No se puede ganar un falta envido diciendole no quiero");
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
        estado.setVisible(true);

        System.out.println("imprime un envido de " + envido);

        if(!esLlamadoDesdeTimer && envido!=0) {
            System.out.println("llama a " + "src/truco_java/cantos/envido/5" + envido + ".wav");
            Music cantar = new Music();
            if(numeroPersonaje==5) cantar.setFile("src/truco_java/cantos/envido/5" + envido + ".wav", 1);
            else cantar.setFile("src/truco_java/cantos/envido/" + envido + ".wav", 1);
            cantar.play();
        }

        switch(envido){
            case -1:
                texto = "Em... No quiero!";
                if(numeroPersonaje==5) texto = "No tea? so No quiero!";
                break;
            case 0:
                fondoEstado.setVisible(false);
                estado.setText("");
                setFondo(0);
                return;
            case 1:
                texto = "Envido! Carajo";
                if(numeroPersonaje==5) texto = "Why not? Envido!";
                break;
            case 2:
                texto = "Aca te va otro: Envido!";
                if(numeroPersonaje==5) texto = "What about another Envido?!";
                break;
            case 3:
                texto = "Seguro? Real Envido!";
                if(numeroPersonaje==5) texto = "I say Real Envido!";
                break;
            case 4:
                if(numeroPersonaje==5) texto = "Falta Envido my mate!";
                texto = "Y Falta Envido?";
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
                            imprimeAIEnvido(envido, true); // Lo vuelve a imprim,ir
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

        if(empateDefine==false && (ganoJugador>=2 || ganoAI>=2)){
            if(ganoJugador>ganoAI)
                ganador=1;
            else
                ganador=2;
        }

        return ganador;
    }


    private int ganaRonda(int pos){
        int rankingJugador = jugador.getCartasJugadas().get(pos).rankingCarta(); // carrta ddel jugador
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

        int desicion = ai.desidirTruco(nivelTruco, jugador);

        if(desicion == nivelTruco && nivelTruco==0) // Si no se canto nada y no quiere truco
            return 0;

        if(desicion==0){ // si no quiere truco la AI
            if (habilitadoARetrucar == 2 && responder) { // SI tiene que contestar
                imprimeAITruco(-1, false);
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                jugador.setPuntaje(jugador.getPuntaje() + calcularTrucoPerdido(), this);
                JOptionPane.showMessageDialog(null, "La PC ha rechazado el Truco. Repartiendo...");
                otraPartida();
                habilitaTurno();
                return 1;
            } else return 0;
        }

        if(desicion == nivelTruco && responder){ // Si acepta el truco
            imprimeAITruco(4, false);
            habilitadoARetrucar=2;
            truco.setEnabled(false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            // Al aceptar truco se deshabilita el truco
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            habilitaTurno();
            return 0;
        }

        if(!responder && desicion<(nivelTruco+1)) // Si no estoy respondiendo y quiero menos de la oferta actual (lo cual no se puede), no hacer nada
            return 0;
        if(responder && desicion<(nivelTruco+1)){ // Si no estoy respondiendo y quiero menos de la oferta actual (lo cual no se puede), no hacer nada
            imprimeAITruco(-1, false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            jugador.setPuntaje(jugador.getPuntaje() + calcularTrucoPerdido(), this);
            JOptionPane.showMessageDialog(null, "La PC ha rechazado el Truco. Repartiendo...");
            otraPartida();
            habilitaTurno();
            return 1;
        }

        // Si retruca, imprime el mensaje y le pasa el mando a jugador a aceptar o aumentar la apuesta
        habilitadoARetrucar=1;
        System.out.println("actualmente el estado del truco es " + nivelTruco + " y la desicion es " + desicion);
        if(desicion>nivelTruco)
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

    private void imprimeAITruco(int trucoMSG, boolean esLlamadoDesdeTimer){
        setFondo(1);
        fondoEstado.setVisible(true);

        System.out.println("llama a imprimir AITruco un " + trucoMSG);

        if(!esLlamadoDesdeTimer && trucoMSG!=0) {
            System.out.println("src/truco_java/cantos/truco/" +  numeroPersonaje + trucoMSG + ".wav");
            Music cantar = new Music();
            if(numeroPersonaje==5) cantar.setFile("src/truco_java/cantos/truco/5"+ trucoMSG + ".wav", 1);
            else cantar.setFile("src/truco_java/cantos/truco/" + trucoMSG + ".wav", 1);
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
                truco.setText("Retruco");
                truco.setEnabled(true);
                break;
            case 2:
                estado.setText("Re truco!");
                if(numeroPersonaje==5) estado.setText("Re truco if you're brave!");
                truco.setText("Vale cuatro");
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
                setFondo(0);
                break;
        }

        final String textoTimer = estado.getText();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if(estado.getText().equals(textoTimer)){
                            if(quieroTruco.isVisible())
                                imprimeAITruco(trucoMSG, true);
                            else imprimeAITruco(0, true);
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

    private void info () {
        System.out.println("***********************************************");
        System.out.println("Cartas AI: " + ai.getMano().size());
        for(int i=0;i<ai.getMano().size();i++)
            System.out.println("     mano " + (i+1) + ": " + ai.getMano().get(i).texto());
        System.out.println("Cartas Tiradas AI: " + ai.getCartasJugadas().size());
        for(int i=0;i<ai.getCartasJugadas().size();i++)
            System.out.println("     Tirada " + (i+1) + ": " + ai.getCartasJugadas().get(i).texto());
        System.out.println("Cartas Jugador: " + jugador.getMano().size());
        System.out.println("Cartas Tiradas Jugador: " + jugador.getCartasJugadas().size());
        System.out.println("Nivel de truco: " + nivelTruco);
        System.out.println("Habilitado a retrucar: " + habilitadoARetrucar);
        System.out.println("***********************************************");
    }
}
