package truco_java;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Truco_Java extends JFrame{

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
        jugar.setBounds(100, 325, 300, 50);
        jugar.setVisible(true);
        fondo.add(jugar);
        jugar.addActionListener((ActionEvent e) -> {
            setVisible(false);
            InterfazJuego juego;
            try {
                ImageIcon icono = new ImageIcon("src/truco_java/fondos/icono.png");
                juego = new InterfazJuego(this);
                juego.setIconImage(icono.getImage());
                juego.setBounds(0,0,500,800);
                juego.setLocationRelativeTo(null);
                juego.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Truco_Java.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Acerca De
        JButton acercaBoton = new JButton("Acerca del Juego");
        acercaBoton.setBounds(100, 400, 300, 50);
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
    }

    public static void main(String[] args) throws IOException {
        ImageIcon icono = new ImageIcon("src/ahorcadomalvinas/images/icono.png");

        Truco_Java menu = new Truco_Java();
        menu.setIconImage(icono.getImage());
        menu.setBounds(0,0,500,500);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);


        Music musicaFondo = new Music();
        musicaFondo.setFile("src/truco_java/musica/fondo.wav");
        musicaFondo.play();
/*

*/
    }
}
