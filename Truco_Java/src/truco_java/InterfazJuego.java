package truco_java;

import java.awt.Color;
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

public class InterfazJuego extends JFrame {

    private static ArrayList<Carta> mazo = new ArrayList<>();
    JLabel fondo;
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
    JButton noQuieroEnv, quieroEnv;
    JTextPane estado;

    public InterfazJuego() throws IOException {
        cargarMazo();
        mezclarMazo();

        setLayout(null);
        setDefaultCloseOperation(3);

        // Fondo
        fondo = new JLabel(new ImageIcon("src/truco_java/Imagenes/fondo.png"));
        fondo.setBounds(0, 0, 500, 800);
        fondo.setVisible(true);
        add(fondo);

        // Mezclar el fondo y el personaje y hacer diferetes fondos. Luego se eligen aleatorios.
        // AI Carta 1
        AIC1 = new JLabel();
        AIC1.setBounds(100, 50, 75, 100);
        AIC1.setVisible(true);
        fondo.add(AIC1);

        // AI Carta 2
        AIC2 = new JLabel();
        AIC2.setBounds(200, 50, 75, 100);
        AIC2.setVisible(true);
        fondo.add(AIC2);

        // AI Carta 3
        AIC3 = new JLabel();
        AIC3.setBounds(300, 50, 75, 100);
        AIC3.setVisible(true);
        fondo.add(AIC3);

        // Persona Carta 1
        PC1 = new JButton();
        PC1.setBounds(10, 450, 155, 200);
        PC1.setVisible(true);
        PC1.setBorderPainted(false);
        fondo.add(PC1);
        PC1.addActionListener((ActionEvent e) -> {
            try {
                tirarCarta(0);
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Persona Carta 2
        PC2 = new JButton();
        PC2.setBounds(170, 450, 155, 200);
        PC2.setVisible(true);
        PC2.setBorderPainted(false);
        fondo.add(PC2);
        PC2.addActionListener((ActionEvent e) -> {
            try {
                tirarCarta(1);
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Persona Carta 3
        PC3 = new JButton();
        PC3.setBounds(330, 450, 155, 200);
        PC3.setVisible(true);
        PC3.setBorderPainted(false);
        fondo.add(PC3);
        PC3.addActionListener((ActionEvent e) -> {
            try {
                tirarCarta(2);
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JButton repartir = new JButton("Repartir");
        repartir.setBounds(0, 0, 100, 100);
        repartir.setVisible(true);
        fondo.add(repartir);
        repartir.addActionListener((ActionEvent e) -> {
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

        /*

    JButton tirar = new JButton("tirar");
    tirar.setBounds(400,0,100,100);
    tirar.setVisible(true);
    fondo.add(tirar);
    tirar.addActionListener((ActionEvent e) -> {
        ai.jugarTurno(jugador);
        try {
            dibujarCartas();
        } catch (IOException ex) {
            Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
        }
              System.out.println("....... (" + ai.getCartasJugadas().size() + ")");
            for(int x=0;x<ai.getCartasJugadas().size();x++){
            System.out.println(ai.getCartasJugadas().get(x).getNumero() + " de " + ai.getCartasJugadas().get(x).getPalo());
            }
    });

         */
        // AI Carta Tirada 1
        AICT1 = new JLabel();
        AICT1.setBounds(130, 200, 70, 80);
        AICT1.setVisible(true);
        fondo.add(AICT1);

        // AI Carta Tirada 2
        AICT2 = new JLabel();
        AICT2.setBounds(210, 200, 70, 80);
        AICT2.setVisible(true);
        fondo.add(AICT2);

        // AI Carta Tirada 3
        AICT3 = new JLabel();
        AICT3.setBounds(290, 200, 70, 80);
        AICT3.setVisible(true);
        fondo.add(AICT3);

        // Persona Carta Tirada 1
        PCT1 = new JLabel();
        PCT1.setBounds(130, 300, 70, 80);
        PCT1.setVisible(true);
        fondo.add(PCT1);

        // Persona Carta Tirada 2
        PCT2 = new JLabel();
        PCT2.setBounds(210, 300, 70, 80);
        PCT2.setVisible(true);
        fondo.add(PCT2);

        // Persona Carta Tirada 3
        PCT3 = new JLabel();
        PCT3.setBounds(290, 300, 70, 80);
        PCT3.setVisible(true);
        fondo.add(PCT3);

        // Boton Truco
        truco = new JButton("Truco");
        truco.setBounds(10, 710, 155, 60);
        truco.setVisible(true);
        truco.setEnabled(false);
        fondo.add(truco);
        truco.addActionListener((ActionEvent e) -> {
            try {
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        estado = new JTextPane();
        estado.setBounds(50,150,400,50);
        estado.setEditable(false);
        estado.setBackground(Color.white);
        estado.setVisible(true);
        fondo.add(estado);
        
        // Boton envido general
        envido = new JButton("Envido");
        envido.setBounds(170, 710, 155, 60);
        envido.setVisible(true);
        envido.setEnabled(true);
        fondo.add(envido);
        envido.addActionListener((ActionEvent e) -> {
            if (envidoEsp.isVisible() == true) {
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
            } else {
                envidoEsp.setVisible(true);
                envidoEnvido.setVisible(true);
                realEnvido.setVisible(true);
                faltaEnvido.setVisible(true);
            }
        });

        // Boton Ir al mazo
        irAlMazo = new JButton("Mazo");
        irAlMazo.setBounds(330, 710, 155, 60);
        irAlMazo.setVisible(true);
        fondo.add(irAlMazo);

        // Boton envido especifico
        envidoEsp = new JButton("Envido");
        envidoEsp.setBounds(10, 645, 117, 60);
        envidoEsp.setVisible(false);
        fondo.add(envidoEsp);
        envidoEsp.addActionListener((ActionEvent e) -> {
            envidosCantados.add(1);
            try {
                AICantaEnvido();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            ImprimeAIEnvido(0);
        });

        // Boton envido-envido
        envidoEnvido = new JButton("Envido Doble");
        envidoEnvido.setBounds(131, 645, 117, 60);
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
            ImprimeAIEnvido(0); /// aca hay un problema. esto imprime un envido. arreglado
        });

        // Boton Real Envido
        realEnvido = new JButton("Real Envido");
        realEnvido.setBounds(252, 645, 117, 60);
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
            ImprimeAIEnvido(0);
        });

        // Boton Falta Envido
        faltaEnvido = new JButton("Falta Envido");
        faltaEnvido.setBounds(373, 645, 117, 60);
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
            ImprimeAIEnvido(0);
        });

        //Botones de Quiero y No quiero envido
        noQuieroEnv = new JButton("No Quiero");
        noQuieroEnv.setBounds(255, 400, 235, 60);
        noQuieroEnv.setVisible(false);
        fondo.add(noQuieroEnv);
        noQuieroEnv.addActionListener((ActionEvent e) -> {
            ai.setPuntaje(ai.getPuntaje() + calcularEnvidoPerdido(jugador.getPuntaje()));

            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            fondo.remove(quieroEnv);
            fondo.remove(noQuieroEnv);
            try {
                // Continua con el juego
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            ImprimeAIEnvido(0);
            
        });

        quieroEnv = new JButton("Quiero");
        quieroEnv.setBounds(10, 400, 235, 60);
        quieroEnv.setVisible(false);
        fondo.add(quieroEnv);
        quieroEnv.addActionListener((ActionEvent e) -> {
            if (jugador.calcularEnvido() > ai.calcularEnvido()) //Si gana el jugador
                jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()));
            if (jugador.calcularEnvido() < ai.calcularEnvido()) // Si gana la AI
                ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()));
            if (jugador.calcularEnvido() == ai.calcularEnvido()) { // Si emparatan...
                if (jugador.isMano() == true) // .. y el jugador es mano
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()));
                else // .. y la AI es mano
                    ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()));
            }

            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);

            try {
                // Continua con el juego
                habilitaTurno();
            } catch (IOException ex) {
                Logger.getLogger(InterfazJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        ImprimeAIEnvido(0);
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

    public void dibujarCartas() throws IOException {
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
        switch (ai.getMano().size()) {
            case 0:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT3.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(2).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 1:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT3.setIcon(null);
                break;
            case 2:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(ai.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(null);
                AICT3.setIcon(null);
                break;
            case 3:
                AICT1.setIcon(null);
                AICT2.setIcon(null);
                AICT3.setIcon(null);
                break;
        }

        switch (jugador.getMano().size()) {
            case 0:
                PCT1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT3.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(2).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 1:
                PCT1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT3.setIcon(null);
                break;
            case 2:
                PCT1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                PCT2.setIcon(null);
                PCT3.setIcon(null);
                break;
            case 3:
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
        jugador.agregarCartaJugada(jugador.getMano().get(pos));

        ArrayList<Carta> temp = jugador.getMano();
        temp.remove(pos);
        jugador.setMano(temp);

        dibujarCartas();
    }

    private void otraPartida() throws IOException {
        // Reinicia variables
        nivelTruco = 0;
        envidosCantados = new ArrayList();
        envidoFinalizado = false;
        dibujarBotones();

        // Limpia las manos
        jugador.setMano(new ArrayList());
        ai.setMano(new ArrayList());
        jugador.setCartasJugadas(new ArrayList());
        ai.setCartasJugadas(new ArrayList());

        mezclarMazo();

        // Oculta y resetea botones
        quieroEnv.setVisible(false);
        noQuieroEnv.setVisible(false);
        envido.setEnabled(false);
        envidoEsp.setVisible(false);
        envidoEnvido.setVisible(false);
        realEnvido.setVisible(false);
        faltaEnvido.setVisible(false);
        ImprimeAIEnvido(0);
        
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

        dibujarCartas();

    }

    private void habilitaTurno() throws IOException {
        if(compruebaSiTerminoPartida()==1) {
            JOptionPane.showMessageDialog(null, "Termino Partida. Gano el Jugador.");
            return;
        }
        if(compruebaSiTerminoPartida()==2) {
            JOptionPane.showMessageDialog(null, "Termino Partida. Gano la PC.");
            return;
        }
        
        if (jugador.getCartasJugadas().isEmpty() && ai.getCartasJugadas().isEmpty()) { // No jugo nadie
            if (jugador.isMano() == true) {
                truco.setEnabled(true);
                envido.setEnabled(true);
                irAlMazo.setEnabled(true);
                PC1.setEnabled(true);
                PC2.setEnabled(true);
                PC3.setEnabled(true);
            } else {
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1.setEnabled(false);
                PC2.setEnabled(false);
                PC3.setEnabled(false);
                if(envidosCantados.size() == 0){
                    if (AICantaEnvido() == 0);
                    else return;
                }
                ai.jugarTurno(jugador, this);
                dibujarCartas();
                habilitaTurno();
            }
        } else if (jugador.getCartasJugadas().isEmpty() && !ai.getCartasJugadas().isEmpty()) { // Ya Jugó la AI. Turno Jugador
            truco.setEnabled(true);
            envido.setEnabled(true);
            irAlMazo.setEnabled(true);
            PC1.setEnabled(true);
            PC2.setEnabled(true);
            PC3.setEnabled(true);
        } else if (!jugador.getCartasJugadas().isEmpty() && ai.getCartasJugadas().isEmpty()) { // Ya Jugó el Jugador. Turno AI
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1.setEnabled(false);
                PC2.setEnabled(false);
                PC3.setEnabled(false);
                if(envidoFinalizado==false){
                    if(AICantaEnvido()==0);
                    else return;
                }
                ai.jugarTurno(jugador, this);
                System.out.println("juega");
                dibujarCartas();
                habilitaTurno();
        } else if (!jugador.getCartasJugadas().isEmpty() && !ai.getCartasJugadas().isEmpty()) { // Siguiente Ronda
            if (jugador.getCartasJugadas().size() == ai.getCartasJugadas().size()) { // Si es una ronda en la que nadie jugó
                int rankingJugador = jugador.getCartasJugadas().get(jugador.getCartasJugadas().size()-1).rankingCarta();
                int rankingAI = ai.getCartasJugadas().get(ai.getCartasJugadas().size()-1).rankingCarta();
                
                if (rankingJugador > rankingAI) { // Si gano jugador en la anterior ronda
                    truco.setEnabled(true);
                    irAlMazo.setEnabled(true);
                    PC1.setEnabled(true);
                    PC2.setEnabled(true);
                    PC3.setEnabled(true);
                } else if (rankingAI > rankingJugador) { // si gano AI en la anterior ronda
                    truco.setEnabled(false);
                    envido.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    PC1.setEnabled(false);
                    PC2.setEnabled(false);
                    PC3.setEnabled(false);
                    ai.jugarTurno(jugador, this);
                    dibujarCartas();
                    habilitaTurno();
                }
            } else if (jugador.getCartasJugadas().size() == ai.getCartasJugadas().size() - 1) { // Si ya la AI tiró en esa ronda
                truco.setEnabled(true);
                envido.setEnabled(true);
                irAlMazo.setEnabled(true);
                PC1.setEnabled(true);
                PC2.setEnabled(true);
                PC3.setEnabled(true);
            } else if (jugador.getCartasJugadas().size() - 1 == ai.getCartasJugadas().size()) { // Si ya el jugador tiró en esa ronda
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);
                PC1.setEnabled(false);
                PC2.setEnabled(false);
                PC3.setEnabled(false);
                ai.jugarTurno(jugador, this);
                dibujarCartas();
                habilitaTurno();
            }
        }
    }

    public int AICantaEnvido() throws IOException {
        if (envidoFinalizado == true)
            return 0;

        int desicion;
        if (envidosCantados.isEmpty())
            desicion = ai.desidirEnvido(0);
        else {
            desicion = ai.desidirEnvido(envidosCantados.get(envidosCantados.size() - 1));
            
            if (desicion == envidosCantados.get(envidosCantados.size() - 1)) { // Si la AI Quiere
                if (jugador.calcularEnvido() > ai.calcularEnvido()) { //Si gana el jugador
                    JOptionPane.showMessageDialog(null, "Has ganado. La PC tenía " + ai.calcularEnvido() + " de envido.");
                    jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()));
                }
                if (jugador.calcularEnvido() < ai.calcularEnvido()) { // Si gana la AI
                    JOptionPane.showMessageDialog(null, "Has perdido. La PC tenía " + ai.calcularEnvido() + " de envido.");
                    ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()));
                }
                if (jugador.calcularEnvido() == ai.calcularEnvido()) { // Si empatan...
                    if (jugador.isMano() == true) { // .. y el jugador es mano
                        JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has ganado por mano");
                        jugador.setPuntaje(jugador.getPuntaje() + calcularEnvidoGanado(ai.getPuntaje()));
                    } else { // .. y la AI es mano
                        JOptionPane.showMessageDialog(null, "Empate (" + jugador.calcularEnvido() + " de envido). Has perdido, la PC es mano");
                        ai.setPuntaje(ai.getPuntaje() + calcularEnvidoGanado(jugador.getPuntaje()));
                    }
                }
                quieroEnv.setVisible(false);
                noQuieroEnv.setVisible(false);
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
                habilitaTurno();
                return 1;
            }
        }
        
        switch (desicion) {
            case 0:
                if(!envidosCantados.isEmpty()){
                    System.out.println("decision: " + desicion + " y la anterior: " + envidosCantados.get(envidosCantados.size() - 1));
                    ImprimeAIEnvido(-1);
                    envidoFinalizado=true;
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
        ImprimeAIEnvido(desicion);

        // Boton Quiero Envido
        quieroEnv.setVisible(true);
        System.out.println("lo pone visiblemente");

        // Boton No Quiero Envido
        noQuieroEnv.setVisible(true);
        
        

        // Continua con el juego
        // habilitaTurno();
        
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
                    total += 3;
                    break;
                case 3:
                    total += 3;
                    break;
                case 4:
                    total += 30 - puntajePerdedor;
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
                    total += 30 - puntajePerdedor;
            }
        }

        if(envidosCantados.get(envidosCantados.size()-1) == 1)
            total+=1;

        envidoFinalizado = true;
        return total;
    }

    private void ImprimeAIEnvido(int envido){
        estado.setVisible(true);
        
        switch(envido){
            case 0:
                estado.setText("");
                estado.setVisible(false);
                break;
            case 1:
                estado.setText("Envido! Carajo");
                break;
            case 2:
                estado.setText("Envido Envido!");
                break;
            case 3:
                estado.setText("Real Envido!");
                break;
            case 4:
                estado.setText("Falta Envido!");
                break;
            case -1:
                estado.setText("No quiero!");
                break;
        }
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
    
    
    /*
    private void sumarPuntosEnvido (){

    }*/
}
