package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Truco_Java extends JFrame{

    public static JCheckBox musica = new JCheckBox("Musica y Sonido", true);
    public JCheckBox facil = new JCheckBox("Modo Fácil (PC no miente)", false);
    public JCheckBox movCartas = new JCheckBox("Modo Rápido", false);
    public static Music musicaFondo = new Music();
    public static int ganadasJugador=0, ganadasAI=0;
    public static JTextPane puntajeAI, puntajeJugador;
    public static JLabel puntajeFondo;
    private boolean facilChecked = false;
    private static final Music efectos = new Music();
    public static ArrayList<Usuario> listaUsuarios = new ArrayList();
    private static JButton sesionBoton;
    private static JButton registrarBoton;
    private static JTextPane bienvenido;
    public static int posUsuario=-1;

    public Truco_Java () throws IOException {

        setLayout(null);
        setDefaultCloseOperation(3);

        // Fondo
        JLabel fondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/menu.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        fondo.setBounds(0, 0, 500, 500);
        fondo.setVisible(true);
        add(fondo);

        // Logo
        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(100, 35, 300, 100);
        logo.setVisible(true);
        fondo.add(logo);

        // Jugar
        JButton jugar = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugarBoton.png")).getScaledInstance(300, 60, Image.SCALE_SMOOTH)));
        jugar.setBounds(100, 330, 300, 60);
        jugar.setVisible(true);
        jugar.setOpaque(false);
        jugar.setContentAreaFilled(false);
        jugar.setBorderPainted(false);
        fondo.add(jugar);
        jugar.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            setVisible(false);
            InterfazJuego juego;
            try {
                juego = new InterfazJuego(this);
                juego.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                juego.setResizable(false);
                juego.setTitle("Juego Truco");
                juego.setBounds(0,0,505,800);
                juego.setLocationRelativeTo(null);
                juego.setVisible(true);
                //Muestra el mensaje que avisa para comenzar el juego
                JOptionPane.showMessageDialog(null, "Aprete el mazo de cartas para comenzar el juego...");
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Acerca De
        JButton acercaBoton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/acercaBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        acercaBoton.setBounds(100, 400, 140, 40);
        acercaBoton.setVisible(true);
        acercaBoton.setOpaque(false);
        acercaBoton.setContentAreaFilled(false);
        acercaBoton.setBorderPainted(false);
        fondo.add(acercaBoton);
        acercaBoton.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            setVisible(false);
            AcercaDe acerca;
            ImageIcon icono = new ImageIcon("src/truco_java/fondos/icono.png");
            try {
                acerca = new AcercaDe(this);
                acerca.setIconImage(icono.getImage());
                acerca.setBounds(0,0,500,500);
                acerca.setTitle("Acerca del Juego");
                acerca.setResizable(false);
                acerca.setLocationRelativeTo(null);
                acerca.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Inicio de Sesion / Salir
        sesionBoton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/iniciarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        sesionBoton.setBounds(410, 340, 75, 40);
        sesionBoton.setVisible(true);
        sesionBoton.setOpaque(false);
        sesionBoton.setContentAreaFilled(false);
        sesionBoton.setBorderPainted(false);
        fondo.add(sesionBoton);
        sesionBoton.addActionListener((ActionEvent e) -> {
            if(posUsuario!=-1){
                try {
                    sesionAccion(false,0);
                } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la cuenta: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                }
                return;
            }
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            Sesion sesion;
            try {
                sesion = new Sesion();
                sesion.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                sesion.setBounds(0,0,500,350);
                sesion.setTitle("Acerca del Juego");
                sesion.setResizable(false);
                sesion.setLocationRelativeTo(null);
                sesion.setUndecorated(true);
                sesion.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Registrarse
        registrarBoton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/registrarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        registrarBoton.setBounds(410, 400, 75, 40);
        registrarBoton.setVisible(true);
        registrarBoton.setOpaque(false);
        registrarBoton.setContentAreaFilled(false);
        registrarBoton.setBorderPainted(false);
        fondo.add(registrarBoton);
        registrarBoton.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            Registrarse sesion;
            try {
                sesion = new Registrarse();
                sesion.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                sesion.setBounds(0,0,500,350);
                sesion.setTitle("Acerca del Juego");
                sesion.setResizable(false);
                sesion.setLocationRelativeTo(null);
                sesion.setUndecorated(true);
                sesion.setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la información acerca del juego: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });

        // Salir
        JButton salir = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/salirBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        salir.setBounds(260, 400, 140, 40);
        salir.setVisible(true);
        salir.setOpaque(false);
        salir.setContentAreaFilled(false);
        salir.setBorderPainted(false);
        fondo.add(salir);
        salir.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            System.exit(0);
        });

        // CheckBox selector de musica ON/OFF
        musica.setBounds(100, 300, 130, 20);
        musica.setOpaque(false);
        fondo.add(musica);
        musica.addItemListener((ItemEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            if(e.getStateChange()==1){
                musicaFondo.setFile("src/truco_java/musica/fondo.wav");
                musicaFondo.play();
            } else{
                try {
                    musicaFondo.stop();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al detener la música: " + ex.getMessage());
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                }
            }
        });

        // CheckBox selector de modo facil/dificil
        facil.setBounds(230, 290, 250, 40);
        facil.setOpaque(false);
        fondo.add(facil);
        facil.addItemListener((ItemEvent e) -> {
            if(e.getStateChange()==1 && !facilChecked){
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                int dialogResult = JOptionPane.showConfirmDialog(null, "Al estar en modo facil, no se sumará puntaje! Este está diseñado exclusivamente para practicar. ¿Aún así desea jugar en modo facil?","Aclaración",JOptionPane.YES_NO_OPTION);
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                if(dialogResult == JOptionPane.NO_OPTION)
                    facil.setSelected(false);
                else {
                    facilChecked=true;
                    facil.setSelected(true);
                }
            }
        });

        // CheckBox de modo rápido
        movCartas.setBounds(100, 270, 250, 40);
        movCartas.setOpaque(false);
        fondo.add(movCartas);

        // Créditos
        JLabel creditos = new JLabel("Creado por Leonardo D.S. - 2022 - Licencia GPL v3.0");
        creditos.setBounds(10, 440, 490, 35);
        creditos.setFont(new Font("Arial", Font.BOLD, 14));
        creditos.setForeground(Color.WHITE);
        creditos.setVisible(true);
        fondo.add(creditos);

        // Fondo puntaje
        puntajeFondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/puntaje/bg0.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
        puntajeFondo.setBounds(10, 325, 75, 100);
        puntajeFondo.setVisible(false);
        fondo.add(puntajeFondo);

        // Puntaje Jugador
        puntajeJugador = new JTextPane();
        puntajeJugador.setText(Integer.toString(ganadasJugador));
        puntajeJugador.setFont(new Font("Arial", Font.BOLD, 20));
        puntajeJugador.setBounds(5, 50, 25, 30);
        puntajeJugador.setVisible(true);
        puntajeJugador.setEditable(false);
        puntajeJugador.setOpaque(false);
        puntajeFondo.add(puntajeJugador);
        //Centra el texto
        StyledDocument doc = puntajeJugador.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Puntaje Ai
        puntajeAI = new JTextPane();
        puntajeAI.setText(Integer.toString(ganadasAI));
        puntajeAI.setFont(new Font("Arial", Font.BOLD, 20));
        puntajeAI.setBounds(42, 50, 25, 30);
        puntajeAI.setEditable(false);
        puntajeAI.setOpaque(false);
        puntajeAI.setVisible(true);
        puntajeFondo.add(puntajeAI);
        //Centra el texto
        StyledDocument doc2 = puntajeAI.getStyledDocument();
        SimpleAttributeSet center2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
        doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);

        // Mensaje de bienvenida al usuario
        bienvenido = new JTextPane();
        bienvenido.setFont(new Font("Arial", Font.BOLD, 20));
        bienvenido.setBounds(100, 10, 300, 35);
        bienvenido.setForeground(Color.WHITE);
        bienvenido.setEditable(false);
        bienvenido.setOpaque(false);
        bienvenido.setVisible(true);
        fondo.add(bienvenido);
        //Centra el texto
        StyledDocument doc3 = bienvenido.getStyledDocument();
        SimpleAttributeSet center3 = new SimpleAttributeSet();
        StyleConstants.setAlignment(center3, StyleConstants.ALIGN_CENTER);
        doc3.setParagraphAttributes(0, doc3.getLength(), center3, false);
    }

    public static void main(String[] args) throws IOException {
        Truco_Java menu;

        try {
            menu = new Truco_Java();
            menu.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
            menu.setBounds(0,0,500,500);
            menu.setResizable(false);
            menu.setTitle("Menu - Truco");
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el menu: " + e.getMessage());
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            return;
        }

        musicaFondo.setFile("src/truco_java/musica/fondo.wav");
        musicaFondo.play();
    }

    public static void sesionAccion (boolean sesionIniciada, int posiUsuario) throws IOException{
        if(!sesionIniciada){
            registrarBoton.setVisible(true);
            sesionBoton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/iniciarBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
            bienvenido.setText(null);
            listaUsuarios.get(posUsuario).encriptaPuntaje();
            Usuario.escribirUsuarios();
            ganadasAI=0;
            ganadasJugador=0;
            puntajeAI.setText(String.valueOf(ganadasAI));
            puntajeJugador.setText(String.valueOf(ganadasJugador));
            puntajeFondo.setVisible(false);
            posUsuario=-1;
            return;
        }
        posUsuario=posiUsuario;
        registrarBoton.setVisible(false);
        sesionBoton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cerrarSesionBoton.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        bienvenido.setText("Bienvenido, " + listaUsuarios.get(posUsuario).getNombre());
        if(listaUsuarios.get(posUsuario).getPuntajeAI()>0 || listaUsuarios.get(posUsuario).getPuntajeJugador()>0)
            puntajeFondo.setVisible(true);
    }
}
