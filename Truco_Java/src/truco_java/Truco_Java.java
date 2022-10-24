package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
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
    public static Music musicaFondo = new Music();
    public int ganadasJugador=0, ganadasAI=0;
    public JTextPane puntajeAI, puntajeJugador;
    public JLabel puntajeFondo;
    private boolean facilChecked = false;
    private static final Music efectos = new Music();

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
        musica.setBounds(100, 290, 130, 40);
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
}
