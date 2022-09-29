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

        // Fondo
        JLabel icono = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/icono.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        icono.setBounds(100, 20, 300, 100);
        icono.setVisible(true);
        fondo.add(icono);

        // Jugar
        JButton jugar = new JButton("Jugar");
        jugar.setBounds(100, 300, 300, 40);
        jugar.setVisible(true);
        fondo.add(jugar);
        jugar.addActionListener((ActionEvent e) -> {
            setVisible(false);
            InterfazJuego juego;
            try {
                juego = new InterfazJuego(this);
                juego.setBounds(0,0,500,800);
                juego.setLocationRelativeTo(null);
                juego.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Truco_Java.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static void main(String[] args) throws IOException {

        Truco_Java menu = new Truco_Java();
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
