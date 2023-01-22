package truco_java;

import java.util.Scanner;
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

public class InterfazServidor extends JFrame {
    // La interfaz Servidor es quien espera los comandos del cliente para actuar
    Servidor server = new Servidor();

    // Swing
    private JLabel fondo = new JLabel();
    private Truco_Java menu;
    private JLabel AIC1, AIC2, AIC3;
    private JLabel AICT1, AICT2, AICT3;
    private JButton PC1, PC2, PC3;
    private JLabel PCT1, PCT2, PCT3;
    private JButton truco, envido, irAlMazo, envidoEsp, envidoEnvido, realEnvido, faltaEnvido;
    private JButton noQuieroEnv, quieroEnv, noQuieroTruco, quieroTruco;
    private JLabel fondoEstado;
    private JLabel puntajeOponente = new JLabel(), puntajeJugador = new JLabel();
    private JTextPane estado;
    private JButton movCarta = new JButton();
    private boolean PC1Enabled=false, PC2Enabled=false, PC3Enabled=false;
    private final Music cantar = new Music();
    private static final Music efectos = new Music();

    // Jugadores
    private Persona jugador = new Persona(null, true);
    private final int numeroPersonaje = new Random().nextInt(6) + 1; // Representa el personaje que fue generado;
    private String nombreOponente ="la PC";
    private Persona oponente = new Persona(null, false);
    private boolean termino = false;

    // Información temporal del juego
    private ArrayList<Integer> envidosCantados = new ArrayList<>();
    private int cantCartasOponente;
    private int nivelTruco;
    private boolean envidoFinalizado;
    private int habilitadoARetrucar; // 2--> Jugador; 1--> Oponente

    public InterfazServidor(Truco_Java menu) throws IOException {
        this.menu = menu;

        setLayout(null);
        setDefaultCloseOperation(3);
        switch(numeroPersonaje){
            case 1: nombreOponente="El Carpincho"; break;
            case 2: nombreOponente="La Roca"; break;
            case 3: nombreOponente="Messi"; break;
            case 4: nombreOponente="El Diego"; break;
            case 5: nombreOponente="Boris"; break;
            case 6: nombreOponente="Guido"; break;
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
        repartir.setEnabled(false);
        fondo.add(repartir);

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
            // efectos.setFile("src/truco_java/musica/boton.wav", 1);
            // efectos.play();
            // JOptionPane.showMessageDialog(null, "Te has ido al mazo. Repartiendo...");
            // efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            // efectos.play();
            // int puntos=0;
            // if(!envidoFinalizado && oponente.getCartasJugadas().isEmpty())
            //     puntos++;
            // oponente.setPuntaje(oponente.getPuntaje()+puntos+calcularTrucoGanado(), this);
            // try {
            //     dibujarPuntaje();
            // } catch (IOException ex) {
            //     JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar el puntaje: " + ex.getMessage());
            //     efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            //     efectos.play();
            // }
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
            truco.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            habilitadoARetrucar=1;
            if(!envidoFinalizado && oponente.getCartasJugadas().isEmpty()){
                if(nivelTruco!=0) nivelTruco--;
                habilitadoARetrucar = 0;
            }
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(server.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                    }
                }
            };
            thread.start();
            imprimeAIEnvido(0,false);
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
            truco.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            habilitadoARetrucar=1;
            if(!envidoFinalizado && oponente.getCartasJugadas().isEmpty()){
                if(nivelTruco!=0) nivelTruco--;
                habilitadoARetrucar = 0;
            }
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(server.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                    }
                }
            };
            thread.start();
            imprimeAIEnvido(0,false);
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
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
            truco.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            habilitadoARetrucar=1;
            if(!envidoFinalizado && oponente.getCartasJugadas().isEmpty()){
                if(nivelTruco!=0) nivelTruco--;
                habilitadoARetrucar = 0;
            }
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(server.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                    }
                }
            };
            thread.start();
            imprimeAIEnvido(0,false);

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
            truco.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            habilitadoARetrucar=1;
            if(!envidoFinalizado && oponente.getCartasJugadas().isEmpty()){
                if(nivelTruco!=0) nivelTruco--;
                habilitadoARetrucar = 0;
            }
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(server.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                    }
                }
            };
            thread.start();
            imprimeAIEnvido(0,false);
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
            envidosCantados.add(5); //El 5 significa que quiere. Procesa el puntaje en el cliente
            truco.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(server.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                    }
                }
            };
            thread.start();
            imprimeAIEnvido(0,false);
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
            envidosCantados.add(-1);
            truco.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(server.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                    }
                }
            };
            thread.start();
            imprimeAIEnvido(0,false);
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
            if(habilitadoARetrucar != 1){
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                truco.setEnabled(false);
                envido.setEnabled(false);
                irAlMazo.setEnabled(false);

                nivelTruco++;
                habilitadoARetrucar = 1;
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            recibirMensaje(server.enviaTruco(nivelTruco, habilitadoARetrucar));
                        } catch(IOException er){
                            System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                        }
                    }
                };
                thread.start();
                imprimeAIEnvido(0,false);
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
            // efectos.setFile("src/truco_java/musica/boton.wav", 1);
            // efectos.play();
            // oponente.setPuntaje(oponente.getPuntaje() + calcularTrucoPerdido(), this);
            // quieroTruco.setVisible(false);
            // noQuieroTruco.setVisible(false);
            // setFondo(0);
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
        puntajeOponente.setBounds(50, 65, 50, 85);
        puntajeOponente.setVisible(true);
        puntajeFondo.add(puntajeOponente);

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
            int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + nombreOponente + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            if(dialogResult == JOptionPane.YES_OPTION){
                try {
                    server.enviaMensaje("retira");
                } catch (Exception ex) {
                    System.out.println("No se pudo enviar el mensaje");
                }
                menu.setVisible(true);
                dispose();
            }
        });

        //Espera al cliente
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Esperando a que el cliente se conecte");
                try{
                    recibirMensaje(server.recibirMensaje());
                } catch(IOException er){
                    System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                }
                System.out.println("Recibe el mensaje");
            }
        };
        thread.start();
    }

    public void dibujarPuntaje() throws IOException {
        if(termino)
            return;
        int jugadorPunt = jugador.getPuntaje();
        int aiPunt = oponente.getPuntaje();
        if(jugadorPunt > 15)
            jugadorPunt = 15;
        if(aiPunt > 15)
            aiPunt = 15;

        puntajeJugador.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + jugadorPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));
        puntajeOponente.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/" + aiPunt + ".png")).getScaledInstance(50, 85, Image.SCALE_SMOOTH)));

    }

    private void dibujarCartas() throws IOException {
        AIC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        AIC2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        AIC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));

        switch (cantCartasOponente) {
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

        ArrayList<JButton> manos = new ArrayList<>();
        manos.add(PC1);
        manos.add(PC2);
        manos.add(PC3);

    for(int i=0;i<jugador.getMano().size();i++){
            if(jugador.getPosMano()[i]==-1){
               manos.get(i).setVisible(false);
               manos.get(i).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            } else {
               manos.get(i).setVisible(true);
               manos.get(i).setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(jugador.getPosMano()[i]).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
            }

        }

        //switch (jugador.getMano().size()) {
        //    case 0:
        //        PC1.setVisible(false);
        //        PC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        PC2.setVisible(false);
        //        PC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        PC3.setVisible(false);
        //        PC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        break;
        //    case 1:
        //        PC1.setVisible(true);
        //        PC1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(0).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        PC2.setVisible(false);
        //        PC2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        PC3.setVisible(false);
        //        PC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        break;
        //    case 2:
        //        PC1.setVisible(true);
        //        PC1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(0).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        PC2.setVisible(true);
        //        PC2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(1).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        PC3.setVisible(false);
        //        PC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        break;
        //    case 3:
        //        PC1.setVisible(true);
        //        PC1.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(0).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        //PC1.setIcon(new ImageIcon(p.getMano().get(0).linkCarta()));
        //        PC2.setVisible(true);
        //        PC2.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(1).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        //PC2.setIcon(new ImageIcon(p.getMano().get(1).linkCarta()));
        //        PC3.setVisible(true);
        //        PC3.setIcon(new ImageIcon(ImageIO.read(new File(jugador.getMano().get(2).linkCarta())).getScaledInstance(155, 200, Image.SCALE_SMOOTH)));
        //        //PC3.setIcon(new ImageIcon(p.getMano().get(2).linkCarta()));
        //        break;
        //}

        // Cartas ya tiradas
        switch (oponente.getCartasJugadas().size()) {
            case 3:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT3.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(2).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT2.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                AICT3.setIcon(null);
                break;
            case 1:
                AICT1.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
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

        if(!menu.movCartas.isSelected()) moverCartaPersona(pos, jugador.getMano().get(jugador.getPosMano()[pos]).linkCarta(), jugador.getCartasJugadas().size());

        //Tira la carta
        jugador.agregarCartaJugada(jugador.getPosMano()[pos]);

        // Indica que carta no se debbe dibujar
        int temp[] = jugador.getPosMano();
        temp[pos] = -1;
        for(int i=pos+1;i<temp.length;i++)
            temp[i]-=1;
        jugador.setPosMano(temp);

        if(menu.movCartas.isSelected()){ //Si no se quiere movimiento de cartas
            try {
                dibujarCartas();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        }

        // Espera la respuesta de HabilitaTurno o TirarCartaa del cliente
        // Thread thread = new Thread(){
        //     public void run(){
        //         System.out.println("SE ABRIO EL SERVER DESDE EL THREAD");
        //         try{
        //             recibirMensaje(server.recibirMensaje());
        //         } catch(IOException er){
        //             System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
        //         }
        //     }
        // };
        // thread.start();
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
            case 5:
                // TODO: INICIAR SERVIDOR PARA PREGUNTAR EL PUNTAJE
                return;
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
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + nombreOponente + " en el truco: " + ex.getMessage());
                                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                    efectos.play();
                                }
                            }
                            else {
                                try {
                                    imprimeAITruco(0, true);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de escribir el mensaje de " + nombreOponente + " en el truco: " + ex.getMessage());
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
        final String archivo = oponente.getCartasJugadas().get(oponente.getCartasJugadas().size()-1).linkCarta();

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
                origenX=300;
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
                destinoX=300;
                break;
            default:
                destinoX=0;
                break;
        }

        if(menu.movCartas.isSelected()){ //Si no se quiere movimiento de cartas
            try {
                dibujarCartas();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            return;
        }

        movCarta.setBounds(origenX, 70, 75, 100);
        movCarta.setVisible(true);
        fondo.add(movCarta);

        int porcentajeMov=40;
        final int movX=(destinoX-origenX)/porcentajeMov, movY=(210-70)/porcentajeMov;
        final int sizeX = (movCarta.getWidth()-70)/porcentajeMov;
        final int sizeY = (movCarta.getHeight()-80)/porcentajeMov;

        Thread thread = new Thread(){
            public void run(){
                timerMovCarta(movX, movY, origenX, 70, destinoX, 210, sizeX, sizeY, archivo, -1);
            }
        };
        thread.start();
    }

    private void moverCartaPersona(int origen, String archivo, int destino) throws IOException{
        final int origenX, destinoX;

        //Según qué carta sea, la oculta y pone una temporal en reemplazo
        switch(origen){
            case 0:
                PC1.setVisible(false);
                origenX=10;
                break;
            case 1:
                PC2.setVisible(false);
                origenX=170;
                break;
            case 2:
                PC3.setVisible(false);
                origenX=330;
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
                timerMovCarta(movX, movY, origenX, 400, destinoX, 310, sizeX, sizeY, archivo, origen);
            }
        };
        thread.start();
    }

  public void timerMovCarta (int movX, int movY, int origenX, int origenY, int destinoX, int destinoY, int ancho, int alto, String archivo, int pos) {
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
          timerMovCarta(movX,movY, origenX+movX, origenY+movY, destinoX, destinoY, ancho, alto, archivo, pos);
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
          if(pos!=-1) {
              server.tirarCarta(pos);
                try{
                    recibirMensaje(server.recibirMensaje());
                } catch(IOException er){
                    System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                }
          }
      } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar las cartas: " + ex.getMessage());
          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
          efectos.play();
      }
  }

  // MENSAJE DEL CLIENTE

    public void recibirMensaje (String mensaje) {
        if(mensaje==""){
            try {
                recibirMensaje(server.recibirMensaje());
            } catch (Exception e) {
                System.out.println("Error de conexion suponga asjdfhaklsdjfhalkjsdhfalksjdhflkashdflkajhsdflkjahsdflkajhsdklfjh");
            }
            return;
        }
        Scanner scanf = new Scanner(mensaje);
        String categoria = scanf.next();
        String cat = "";

        for(int i=2;i<categoria.length();i++){
            cat+=categoria.charAt(i);
        }
        // cat = String.copyValueOf()
        System.out.println(categoria);
        System.out.println(cat);

        switch(cat){
            case "update":
                System.out.println("Llego la actualizacion");
                int nuevaCantCartasOponente=Integer.parseInt(scanf.next());
                System.out.println("Nuevo: " + nuevaCantCartasOponente + "; Viejo: " + cantCartasOponente);
                boolean animarOponente=false;
                if(nuevaCantCartasOponente<cantCartasOponente)
                    animarOponente = true;
                cantCartasOponente=nuevaCantCartasOponente;

                ArrayList<Carta> tempJugadasOponente = new ArrayList<>();
                for(int i=0;i<3-cantCartasOponente;i++){
                    tempJugadasOponente.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                    System.out.println("JugadasOponente " + i + ": " + tempJugadasOponente.get(i).texto());
                }
                oponente.setCartasJugadas(tempJugadasOponente);

                ArrayList<Carta> tempMano = new ArrayList<>();
                int sizeMano = Integer.parseInt(scanf.next());
                System.out.println("Cantidad de Cartas en mano: " + sizeMano);
                for(int i=0;i<sizeMano;i++){
                    tempMano.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                    System.out.println("Mano " + i + ": " + tempMano.get(i).texto());
                }
                jugador.setMano(tempMano);

                int[] tempPosMano = new int[3];
                for(int i=0;i<3;i++)
                    tempPosMano[i]=Integer.parseInt(scanf.next());
                jugador.setPosMano(tempPosMano);

                ArrayList<Carta> tempJugadas = new ArrayList<>();
                for(int i=0;i<3-sizeMano;i++)
                    tempJugadas.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                jugador.setCartasJugadas(tempJugadas);

                nivelTruco=Integer.parseInt(scanf.next());
                envidoFinalizado=Boolean.parseBoolean(scanf.next());
                habilitadoARetrucar=Integer.parseInt(scanf.next());

                if(animarOponente){

                    try {
                        moverCartaAI(nuevaCantCartasOponente, 2-nuevaCantCartasOponente);
                    } catch (Exception e) {
                        System.out.println("No se pudo hacer la animacion de la carta: " + e.getMessage());
                    }
                }

                if(Boolean.parseBoolean(scanf.next())){// Es el turno del jugador
                    PC1Enabled=true;
                    PC2Enabled=true;
                    PC3Enabled=true;
                    try {
                        dibujarBotones();
                    } catch (Exception e) {
                        System.out.println("Error al dibujar botones");
                    }
                    if(habilitadoARetrucar==2 || habilitadoARetrucar==0)
                        truco.setEnabled(true);
                    else
                        truco.setEnabled(false);
                    // Si la ultima carta es un 4 no le puede retrucar
                    if(oponente.getCartasJugadas().size() == 3){
                        if(oponente.getCartasJugadas().get(2).rankingCarta()==0) {
                            truco.setEnabled(false);
                        }
                    }

                    if(jugador.getCartasJugadas().isEmpty() && !envidoFinalizado)
                        envido.setEnabled(true);
                    else
                        envido.setEnabled(false);

                    irAlMazo.setEnabled(true);
                } else{ // Turno Oponente
                    PC1Enabled=false;
                    PC2Enabled=false;
                    PC3Enabled=false;
                    truco.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    envido.setEnabled(false);

                    //Espera al cliente
                    Thread thread = new Thread(){
                        public void run(){
                            System.out.println("SE ABRIO EL SERVER DESDE EL THREAD");
                            try{
                                recibirMensaje(server.recibirMensaje());
                            } catch(IOException er){
                                System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                            }
                        }
                    };
                    thread.start();
                }

                    if(!animarOponente){
                        try {
                            dibujarCartas();
                        } catch (Exception e) {
                            System.out.println("error al dibujar las cartas");
                        }
                    }
                oponente.setPuntaje(Integer.parseInt(scanf.next()), this);
                jugador.setPuntaje(Integer.parseInt(scanf.next()), this);
                break;
            case "e":
                int nivel = Integer.parseInt(scanf.next());
                nivelTruco = Integer.parseInt(scanf.next());
                habilitadoARetrucar = Integer.parseInt(scanf.next());
                imprimeAIEnvido(nivel, false);
                setFondo(1);

                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
                irAlMazo.setEnabled(false);
                truco.setEnabled(false);
                envido.setEnabled(false);

                ArrayList<JButton> botones = new ArrayList<>();
                botones.add(envidoEsp);
                botones.add(envidoEnvido);
                botones.add(realEnvido);
                botones.add(faltaEnvido);
                for(int i=0;i<nivel;i++)
                    botones.get(i).setVisible(false);
                for(int i=nivel;i<4;i++){
                    botones.get(i).setVisible(true);
                    botones.get(i).setEnabled(true);
                }
                quieroEnv.setVisible(true);
                noQuieroEnv.setVisible(true);
                break;
            case "imprimir":
                String aImprimir="";
                while(scanf.hasNext()){
                    String temp = scanf.next() + " ";
                    if(!temp.equals("ç "))
                        aImprimir+=temp;
                    else break;
                }
                Thread thread = new Thread(){
                    public void run(){
                        System.out.println("Esperando a que el cliente se conecte");
                        try{
                            recibirMensaje(server.recibirMensaje());
                        } catch(IOException er){
                            System.out.println("Error en la reconexión con el servidor: " + er.getMessage());
                        }
                        System.out.println("Recibe el mensaje");
                    }
                };
                final String imprime = aImprimir;
                thread.start();
                Thread thread2 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, imprime);
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                };
                thread2.start();
                break;
            case "kill":
                menu.setVisible(true);
                dispose();
                break;
            case "t":
                nivelTruco = Integer.parseInt(scanf.next());
                habilitadoARetrucar = Integer.parseInt(scanf.next());

                // Si no se canto envido y es la primer ronda
                if(!envidoFinalizado && oponente.getCartasJugadas().isEmpty())
                    envido.setEnabled(true);

                try {
                    imprimeAITruco(nivelTruco, false);
                } catch (Exception e) {
                    System.out.println("No se pudo dibujar las cartas o reproducir las voces");
                }
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);

    //     if(desicion == nivelTruco && responder){ // Si acepta el truco
    //         habilitadoARetrucar=2;
    //         truco.setEnabled(false);
    //         quieroTruco.setVisible(false);
    //         noQuieroTruco.setVisible(false);
    //         // Al aceptar truco se deshabilita el envido
    //         envido.setEnabled(false);
    //         envidoEsp.setVisible(false);
    //         envidoEnvido.setVisible(false);
    //         realEnvido.setVisible(false);
    //         faltaEnvido.setVisible(false);
    //         // Habilita las cartas
    //         PC1Enabled=true;
    //         PC2Enabled=true;
    //         PC3Enabled=true;
    //         imprimeAITruco(4, false);
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
                break;
            case "retira":
                JOptionPane.showMessageDialog(null, "El oponente " + nombreOponente + " se ha retirado.");
                oponente.setPuntaje(15, this);
                break;
            default:
                System.out.println("No se detecto la categoria del mensaje: " + cat);
                for(int i=0;i<cat.length();i++)
                    System.out.println(i+": "+cat.charAt(i));
                break;
        }
        //
        // //Elimina un caracter de basura más y entra de vuelta al switch
        // categoria="";
        // for(int i=1;i<cat.length();i++)
        //     categoria+=cat.charAt(i);
        //
        // switch(categoria){
        //     default:
        //         System.out.println("No se detecto la categoria del mensaje: " + cat);
        //         for(int i=0;i<cat.length();i++)
        //             System.out.println(i+": "+cat.charAt(i));
        //         break;
        // }
        scanf.close();
    }
}
