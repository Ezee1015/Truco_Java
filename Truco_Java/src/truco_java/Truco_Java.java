package truco_java;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        JButton jugar = new JButton("Jugar");
        jugar.setBounds(100, 350, 300, 40);
        jugar.setVisible(true);
        fondo.add(jugar);
        jugar.addActionListener((ActionEvent e) -> {
            setVisible(false);
            InterfazJuego juego;
            try {
                juego = new InterfazJuego(this);
                juego.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                juego.setTitle("Juego Truco");
                juego.setBounds(0,0,510,800);
                juego.setLocationRelativeTo(null);
                juego.setVisible(true);
                //Muestra el mensaje que avisa para comenzar el juego
                JOptionPane.showMessageDialog(null, "Aprete el mazo de cartas para comenzar el juego...");
            } catch (IOException ex) {
                Logger.getLogger(Truco_Java.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Acerca De
        JButton acercaBoton = new JButton("Acerca del Juego");
        acercaBoton.setBounds(100, 400, 140, 40);
        acercaBoton.setVisible(true);
        fondo.add(acercaBoton);
        acercaBoton.addActionListener((ActionEvent e) -> {
            setVisible(false);
            AcercaDe acerca;
            ImageIcon icono = new ImageIcon("src/truco_java/fondos/icono.png");
            try {
                acerca = new AcercaDe(this);
                acerca.setIconImage(icono.getImage());
                acerca.setBounds(0,0,500,500);
                acerca.setLocationRelativeTo(null);
                acerca.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Truco_Java.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Salir
        JButton salir = new JButton("SALIR");
        salir.setBounds(260, 400, 140, 40);
        salir.setVisible(true);
        fondo.add(salir);
        salir.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        // CheckBox selector de musica ON/OFF
        musica.setBounds(100, 300, 130, 40);
        musica.setOpaque(false);
        fondo.add(musica);
        musica.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange()==1){
                    musicaFondo.setFile("src/truco_java/musica/fondo.wav");
                    musicaFondo.play();
                } else{
                    try {
                        musicaFondo.stop();
                    } catch (IOException ex) {
                        System.out.println("Error al detener la musica");
                    }
                }
             }
          });

        // CheckBox selector de modo facil/dificil
        facil.setBounds(230, 300, 250, 40);
        facil.setOpaque(false);
        fondo.add(facil);
        //facil.addItemListener(new ItemListener() {
        //     public void itemStateChanged(ItemEvent e) {
        //        if(e.getStateChange()==1){
        //            ////////
        //        } else{
        //            ////////
        //        }
        //     }
        //  });
    }

    public static void main(String[] args) throws IOException {

        Truco_Java menu = new Truco_Java();
        menu.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
        menu.setBounds(0,0,500,500);
        menu.setTitle("Menu - Truco");
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);


        musicaFondo.setFile("src/truco_java/musica/fondo.wav");
        musicaFondo.play();
    }
}
