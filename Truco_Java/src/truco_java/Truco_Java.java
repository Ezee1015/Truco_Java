package truco_java;

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

public class Truco_Java extends JFrame{

    static JCheckBox musica = new JCheckBox("Musica y Sonido", true);
    static JCheckBox facil = new JCheckBox("Modo Fácil (PC no miente)", false);
    static Music musicaFondo = new Music();

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
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
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
            System.exit(0);
        });

        // CheckBox selector de musica ON/OFF
        musica.setBounds(100, 290, 130, 40);
        musica.setOpaque(false);
        fondo.add(musica);
        musica.addItemListener((ItemEvent e) -> {
            if(e.getStateChange()==1){
                musicaFondo.setFile("src/truco_java/musica/fondo.wav");
                musicaFondo.play();
            } else{
                try {
                    musicaFondo.stop();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ha sucedido un error al detener la música: " + ex.getMessage());
                }
            }
        });

        // CheckBox selector de modo facil/dificil
        facil.setBounds(230, 290, 250, 40);
        facil.setOpaque(false);
        fondo.add(facil);

        // Cŕeditos
        JLabel creditos = new JLabel("Creado por Leonardo D.S. - 2022 - Licencia GPL v3.0");
        creditos.setBounds(95, 439, 490, 30);
        creditos.setFont(new Font("Serif", Font.ITALIC, 13));
        creditos.setVisible(true);
        fondo.add(creditos);
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
            return;
        }

        musicaFondo.setFile("src/truco_java/musica/fondo.wav");
        musicaFondo.play();
    }
}
