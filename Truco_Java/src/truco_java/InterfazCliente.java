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
    // La interfaz cliente es quien espera los comandos del servidor para actuar
    Cliente client;

    // Swing
    private JLabel fondo = new JLabel();
    private Truco_Java menu;
    private MenuJugar menuJugar;
    private JLabel OC1, OC2, OC3;
    private JLabel OCT1, OCT2, OCT3;
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
    private JLabel fondoConexion = new JLabel(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));

    // Jugadores
    private Persona jugador = new Persona(null, true);
    private int numeroPersonaje; // Representa el personaje que fue generado;
    private String nombreOponente ="la PC";
    private Persona oponente = new Persona(null, false);
    private boolean termino = false;
    JLabel puntajeFondo = new JLabel();

    // Información temporal del juego
    private ArrayList<Integer> envidosCantados = new ArrayList<>();
    private int cantCartasOponente;
    private int nivelTruco;
    private boolean envidoFinalizado;
    // public boolean conectado = false;
    private int habilitadoARetrucar; // 2--> Jugador; 1--> Oponente

    public InterfazCliente(Truco_Java menu, String ip, int puerto, MenuJugar menuJugar) throws IOException {
        client = new Cliente(ip,puerto);
        this.menu = menu;
        this.menuJugar = menuJugar;

        Thread esperarThread = new Thread(){
            public void run(){
                try{
                    recibirMensaje(client.recibirMensaje());
                } catch (Exception e) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el nombre del Jugador: " + e.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
                setFondo(0);
                try{
                    recibirMensaje(client.recibirMensaje());
                } catch (Exception e) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al recibir el mensaje: " + e.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
            }
        };
        esperarThread.start();

        setLayout(null);
        setDefaultCloseOperation(3);

        // Inicializa la carta de movimiento
        movCarta.setOpaque(false);
        movCarta.setContentAreaFilled(false);
        movCarta.setBorderPainted(false);

        // Fondo
        fondo.setBounds(0, 0, 500, 800);
        fondo.setVisible(true);
        add(fondo);

        // Oponente Carta 1
        OC1 = new JLabel();
        OC1.setBounds(100, 70, 75, 100);
        OC1.setVisible(true);
        fondo.add(OC1);

        // Oponente Carta 2
        OC2 = new JLabel();
        OC2.setBounds(200, 70, 75, 100);
        OC2.setVisible(true);
        fondo.add(OC2);

        // Oponente Carta 3
        OC3 = new JLabel();
        OC3.setBounds(300, 70, 75, 100);
        OC3.setVisible(true);
        fondo.add(OC3);

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
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
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
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
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
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            truco.setEnabled(false);
            irAlMazo.setEnabled(false);
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

        // Oponente Carta Tirada 1
        OCT1 = new JLabel();
        OCT1.setBounds(130, 210, 70, 80);
        OCT1.setVisible(true);
        fondo.add(OCT1);

        // Oponente Carta Tirada 2
        OCT2 = new JLabel();
        OCT2.setBounds(210, 210, 70, 80);
        OCT2.setVisible(true);
        fondo.add(OCT2);

        // Oponente Carta Tirada 3
        OCT3 = new JLabel();
        OCT3.setBounds(290, 210, 70, 80);
        OCT3.setVisible(true);
        fondo.add(OCT3);

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

        // Fondo de la respuesta del Oponente
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
            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaIrAlMazo());
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                }
            };
            thread.start();
            JOptionPane.showMessageDialog(null, "Te has ido al mazo.");
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
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
            try {
                nivelTruco=0;
                dibujarBotones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            habilitadoARetrucar=1;

            nivelTruco=0;
            habilitadoARetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
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
            try {
                nivelTruco=0;
                dibujarBotones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            habilitadoARetrucar=1;

            nivelTruco=0;
            habilitadoARetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
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
            try {
                nivelTruco=0;
                dibujarBotones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            irAlMazo.setEnabled(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            habilitadoARetrucar=1;

            nivelTruco=0;
            habilitadoARetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
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
            try {
                nivelTruco=0;
                dibujarBotones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
            habilitadoARetrucar=1;

            nivelTruco=0;
            habilitadoARetrucar = 0;

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
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
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
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
            try {
                nivelTruco=0;
                dibujarBotones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            quieroEnv.setVisible(false);
            noQuieroEnv.setVisible(false);
            irAlMazo.setEnabled(false);
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaEnvido(envidosCantados, nivelTruco, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
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
            if(habilitadoARetrucar != 1){
                envidoFinalizado = true;
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                truco.setEnabled(false);
                envido.setEnabled(false);
                envidoEsp.setVisible(false);
                envidoEnvido.setVisible(false);
                realEnvido.setVisible(false);
                faltaEnvido.setVisible(false);
                irAlMazo.setEnabled(false);
                PC1Enabled=false;
                PC2Enabled=false;
                PC3Enabled=false;
                fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

                nivelTruco++;
                habilitadoARetrucar = 1;
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            recibirMensaje(client.enviaTruco(nivelTruco, habilitadoARetrucar));
                        } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                        }
                    }
                };
                thread.start();
                try {
                    imprimeAITruco(0, false);
                } catch (Exception ex) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + ex.getMessage());
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
            envido.setEnabled(false);
            envidoEsp.setVisible(false);
            envidoEnvido.setVisible(false);
            realEnvido.setVisible(false);
            faltaEnvido.setVisible(false);
            truco.setEnabled(false);
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            irAlMazo.setEnabled(false);
            envidoFinalizado=true;
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaTruco(4, habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                }
            };
            thread.start();
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
            quieroTruco.setVisible(false);
            noQuieroTruco.setVisible(false);
            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));

            Thread thread = new Thread(){
                public void run(){
                    try{
                        recibirMensaje(client.enviaTruco(-1,habilitadoARetrucar));
                    } catch(IOException er){
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                }
            };
            thread.start();
            setFondo(0);
        });

        // Fondo puntaje
        puntajeFondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ numeroPersonaje +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
        puntajeFondo.setBounds(390, 10, 100, 150);
        puntajeFondo.setVisible(true);
        fondo.add(puntajeFondo);

        // Puntaje Jugador
        puntajeJugador.setBounds(0, 65, 50, 85);
        puntajeJugador.setVisible(true);
        puntajeFondo.add(puntajeJugador);

        // Puntaje Oponente
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

            // Si se quiere salir en medio de la partida
            int dialogResult = JOptionPane.showConfirmDialog (null, "Está seguro que desea abandonar la partida?\nSe declarará a " + nombreOponente + " como ganador...","Atención!",JOptionPane.YES_NO_OPTION);
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            if(dialogResult == JOptionPane.YES_OPTION){
                try {
                    client.enviaMensaje("retira");
                } catch (Exception ex) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al enviar el mensaje: " + ex.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
                menuJugar.setVisible(true);
                dispose();
            }
        });

        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
        fondoConexion.setBounds(0,730,500,50);
        fondoConexion.setOpaque(false);
        fondoConexion.setVisible(true);
        fondo.add(fondoConexion);
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
        OC1.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        OC2.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        OC3.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));

        switch (cantCartasOponente) {
            case 0:
                OC1.setVisible(false);
                OC2.setVisible(false);
                OC3.setVisible(false);
                break;
            case 1:
                OC1.setVisible(true);
                OC2.setVisible(false);
                OC3.setVisible(false);
                break;
            case 2:
                OC1.setVisible(true);
                OC2.setVisible(true);
                OC3.setVisible(false);
                break;
            case 3:
                OC1.setVisible(true);
                OC2.setVisible(true);
                OC3.setVisible(true);
                break;
        }

        ArrayList<JButton> manos = new ArrayList<>();
        manos.add(PC1);
        manos.add(PC2);
        manos.add(PC3);

        for(int i=0;i<3;i++){
            if(jugador.getPosMano()[i]<0){
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
                OCT1.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                OCT2.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                OCT3.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(2).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                break;
            case 2:
                OCT1.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                OCT2.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(1).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                OCT3.setIcon(null);
                break;
            case 1:
                OCT1.setIcon(new ImageIcon(ImageIO.read(new File(oponente.getCartasJugadas().get(0).linkCarta())).getScaledInstance(70, 80, Image.SCALE_SMOOTH)));
                OCT2.setIcon(null);
                OCT3.setIcon(null);
                break;
            case 0:
                OCT1.setIcon(null);
                OCT2.setIcon(null);
                OCT3.setIcon(null);
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
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            client.throwCard(pos);
                            recibirMensaje(client.receiveMessage());
                        } catch(IOException er){
                            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efectos.play();
                        }
                    }
                };
                thread.start();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        }
    }

    private void imprimeAIEnvido(int envido, boolean esLlamadoDesdeTimer){
        String texto = " ";
        fondoEstado.setVisible(true);
        estado.setFont(new Font("Serif", Font.ITALIC, 30));
        estado.setVisible(true);

        if(!esLlamadoDesdeTimer && envido!=0) {
            if(envido==5) cantar.setFile("src/truco_java/cantos/truco/" + numeroPersonaje + "4.wav", 1);
            else cantar.setFile("src/truco_java/cantos/envido/" + numeroPersonaje + envido + ".wav", 1);
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
                texto = "Quiero!";
                if(numeroPersonaje==5) texto = "Easy peasy. Quiero!";
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
                truco.setEnabled(false);
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
            estadoPersChar = 'a'; // Personaje Oponente normal
        else
            estadoPersChar = 'b'; // personaje Oponente Pregunta (truco, envido, o retrucar cualquiera de las anteriores)

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
                OC1.setVisible(false);
                origenX=100;
                break;
            case 1:
                OC2.setVisible(false);
                origenX=180;
                break;
            case 2:
                OC3.setVisible(false);
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
              Thread thread = new Thread(){
                  public void run(){
                      try{
                          client.tirarCarta(pos);
                          recibirMensaje(client.recibirMensaje());
                      } catch(IOException er){
                          fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          efectos.play();
                      }
                  }
              };
              thread.start();
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
              recibirMensaje(client.recibirMensaje());
          } catch (Exception e) {
              fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
              JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + e.getMessage());
              efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
              efectos.play();
          }
          return;
      }
      Scanner scanf = new Scanner(mensaje.trim());
      String cat="";
      if(scanf.hasNext()) cat = scanf.next();
      else {
          fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
          JOptionPane.showMessageDialog(null, "Ha sucedido un error en la comunicación. Mensaje corrupto");
          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
          efectos.play();
          Thread thread = new Thread(){
              public void run(){
                  try{
                      recibirMensaje(client.recibirMensaje());
                  } catch(IOException er){
                      fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                      JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                      efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                      efectos.play();
                  }
              }
          };
          thread.start();
          return;
      }

        switch(cat){
            case "update":
                int nuevaCantCartasOponente=Integer.parseInt(scanf.next());
                boolean animarOponente=false;
                if(nuevaCantCartasOponente<cantCartasOponente)
                    animarOponente = true;
                cantCartasOponente=nuevaCantCartasOponente;

                ArrayList<Carta> tempJugadasOponente = new ArrayList<>();
                for(int i=0;i<3-cantCartasOponente;i++)
                    tempJugadasOponente.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
                oponente.setCartasJugadas(tempJugadasOponente);

                ArrayList<Carta> tempMano = new ArrayList<>();
                int sizeMano = Integer.parseInt(scanf.next());
                for(int i=0;i<sizeMano;i++)
                    tempMano.add(new Carta(Integer.parseInt(scanf.next()), scanf.next()));
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
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                }

                if(Boolean.parseBoolean(scanf.next())){// Es el turno del jugador
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoJugador.png"));
                    PC1Enabled=true;
                    PC2Enabled=true;
                    PC3Enabled=true;
                    try {
                        dibujarBotones();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                    if((habilitadoARetrucar==2 || habilitadoARetrucar==0) && nivelTruco!=3)
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
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoOponente.png"));
                    PC1Enabled=false;
                    PC2Enabled=false;
                    PC3Enabled=false;
                    truco.setEnabled(false);
                    irAlMazo.setEnabled(false);
                    envido.setEnabled(false);

                    //Espera al cliente
                    Thread thread = new Thread(){
                        public void run(){
                            try{
                                recibirMensaje(client.recibirMensaje());
                            } catch(IOException er){
                                fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                efectos.play();
                            }
                        }
                    };
                    thread.start();
                }

                if(!animarOponente){
                    try {
                        dibujarCartas();
                    } catch (Exception e) {
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                }
                oponente.setPuntaje(Integer.parseInt(scanf.next()), this);
                jugador.setPuntaje(Integer.parseInt(scanf.next()), this);
                try {
                    dibujarPuntaje();
                } catch (Exception e) {
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                }
                break;
            case "envido":
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
                quieroTruco.setVisible(false);
                noQuieroTruco.setVisible(false);
                envido.setEnabled(false);

                ArrayList<JButton> botones = new ArrayList<>();
                botones.add(envidoEsp);
                botones.add(envidoEnvido);
                botones.add(realEnvido);
                botones.add(faltaEnvido);

                if(nivel==5 || nivel==-1){
                    quieroEnv.setVisible(false);
                    noQuieroEnv.setVisible(false);
                    envido.setEnabled(false);
                    for(int i=0;i<botones.size();i++)
                        botones.get(i).setVisible(false);
                    Thread thread = new Thread(){
                        public void run(){
                            try{
                                recibirMensaje(client.recibirMensaje());
                            } catch(IOException er){
                                fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                efectos.play();
                            }
                        }
                    };
                    thread.start();
                    return;
                }

                fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));

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
                final String imprime = aImprimir;
                Thread thread2 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, imprime);
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                };
                thread2.start();
                Thread thread = new Thread(){
                    public void run(){
                        try{
                            recibirMensaje(client.recibirMensaje());
                        } catch(IOException er){
                            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efectos.play();
                        }
                    }
                };
                thread.start();
                break;
            case "kill":
                menuJugar.setVisible(true);
                dispose();
                break;
            case "truco":
                int nivelTrucoTemp = Integer.parseInt(scanf.next());
                habilitadoARetrucar = Integer.parseInt(scanf.next());

                // Si no se canto envido y es la primer ronda
                if(!envidoFinalizado && jugador.getCartasJugadas().isEmpty())
                    envido.setEnabled(true);

                fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoAtencion.png"));
                try {
                    imprimeAITruco(nivelTrucoTemp, false);
                } catch (Exception e) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + e.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
                if(nivelTrucoTemp!=4 && nivelTrucoTemp!=-1){
                    quieroTruco.setVisible(true);
                    noQuieroTruco.setVisible(true);
                    nivelTruco=nivelTrucoTemp;
                    try {
                        dibujarBotones();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes o sonidos: " + e.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                    }
                } else {
                    truco.setEnabled(false);
                    quieroTruco.setVisible(false);
                    noQuieroTruco.setVisible(false);
                    if(nivelTrucoTemp==-1){
                        Thread thread3 = new Thread(){
                            public void run(){
                                JOptionPane.showMessageDialog(null, "" + nombreOponente + " ha rechazado el Truco. Repartiendo...");
                                efectos.setFile("src/truco_java/musica/boton.wav", 1);
                                efectos.play();
                            }
                        };
                        thread3.start();
                    }
                    Thread thread1 = new Thread(){
                        public void run(){
                            try{
                                recibirMensaje(client.recibirMensaje());
                            } catch(IOException er){
                                fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                                JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                                efectos.play();
                            }
                        }
                    };
                    thread1.start();
                }
                break;
            case "puntaje":
                oponente.setPuntaje(Integer.parseInt(scanf.next()), this);
                jugador.setPuntaje(Integer.parseInt(scanf.next()), this);
                try {
                    dibujarPuntaje();
                } catch (Exception e) {
                        fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                        efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        efectos.play();
                }
                Thread thread3 = new Thread(){
                    public void run(){
                        try{
                            recibirMensaje(client.recibirMensaje());
                        } catch(IOException er){
                            fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                            JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + er.getMessage());
                            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                            efectos.play();
                        }
                    }
                };
                thread3.start();
                break;
            case "retira":
                oponente.setPuntaje(15, this);
                JOptionPane.showMessageDialog(null, "El oponente " + nombreOponente + " se ha retirado. Has ganado!");
                menuJugar.setVisible(true);
                dispose();
                break;
            case "persona":
                numeroPersonaje = Integer.parseInt(scanf.next());
                nombreOponente="";
                while(scanf.hasNext()){
                    String nombreTemp = scanf.next();
                    if(!nombreTemp.equals("ç")) nombreOponente += nombreTemp;
                    if(scanf.hasNext()) nombreOponente+=" ";
                }
                String nombre = "";
                if(Truco_Java.posUsuario!=-1)
                    nombre = Truco_Java.listaUsuarios.get(Truco_Java.posUsuario).getNombre();

                Thread thread1 = new Thread(){
                    public void run(){
                        JOptionPane.showMessageDialog(null, nombreOponente + " ha entrado en la sala. Esperando a que el oponente reparta...");
                        efectos.setFile("src/truco_java/musica/boton.wav", 1);
                        efectos.play();
                    }
                };
                thread1.start();
                try {
                    client.enviaPersona(MenuJugar.numeroJugador+1, nombre);
                } catch (Exception e) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error en la conexión: " + e.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
                try {
                    puntajeFondo.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg"+ numeroPersonaje +".png")).getScaledInstance(100, 150, Image.SCALE_SMOOTH)));
                } catch (Exception e) {
                    fondoConexion.setIcon(new ImageIcon("src/truco_java/fondos/turnoError.png"));
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar imágenes: " + e.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
                break;
            default:
                // System.out.println("No se detecto la categoria del mensaje: " + cat);
                // for(int i=0;i<cat.length();i++)
                //     System.out.println(i+": "+cat.charAt(i));
                //Repite el mensaje con un caracter menos (caracteres basura del Socket)
                String catTemp="";
                for(int i=1;i<cat.length();i++){
                    catTemp+=cat.charAt(i);
                }
                String mensajeTemp = catTemp + " ";
                while(scanf.hasNext())
                    mensajeTemp+=scanf.next() + " ";
                if(!mensaje.equals(" ")) recibirMensaje(mensajeTemp);
                break;
        }
        scanf.close();
  }
}
