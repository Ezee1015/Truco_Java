package truco_java;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InterfazJuego extends JFrame {
    
    JLabel AIC1, AIC2, AIC3;

  public InterfazJuego () throws IOException {
    setLayout(null);
    setDefaultCloseOperation(3);

    // Fondo
    JLabel fondo = new JLabel(new ImageIcon("src/Truco_Java/imagenes/fondo.png"));
    fondo.setBounds(0, 0, 500, 800);
    fondo.setVisible(true);
    add(fondo);
    
    // Mezclar el fondo y el personaje y hacer diferetes fonndos. Luego se eligen aleatorios.
    
    /*
    // Personaje
    JLabel AI = new JLabel(new ImageIcon("src/Truco_Java/imagenes/AI1.png"));
    AI.setBounds(200, 0, 100, 100);
    AI.setVisible(true);
    fondo.add(AI);
    */
    // AI Carta 1
    AIC1 = new JLabel(new ImageIcon(ImageIO.read(new File("src/Truco_Java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
    AIC1.setBounds(100, 80, 75, 100);
    AIC1.setVisible(true);
    fondo.add(AIC1);
    
    // AI Carta 2
    AIC2 = new JLabel(new ImageIcon(ImageIO.read(new File("src/Truco_Java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
    AIC2.setBounds(200, 80, 75, 100);
    AIC2.setVisible(true);
    fondo.add(AIC2);
    
    // AI Carta 3
    AIC3 = new JLabel(new ImageIcon(ImageIO.read(new File("src/Truco_Java/mazo/reverso.png")).getScaledInstance(75, 100, Image.SCALE_SMOOTH)));
    AIC3.setBounds(300, 80, 75, 100);
    AIC3.setVisible(true);
    fondo.add(AIC3);
    
    
    
  }

}
