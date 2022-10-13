package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class AcercaDe extends JFrame {
      JTextPane descripcion;
      JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
      JButton adelante = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/adelante.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
      JButton salir = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/volverBoton.png")).getScaledInstance(220, 50, Image.SCALE_SMOOTH)));


  public AcercaDe (Truco_Java menu) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);

        // Fondo
        JLabel fondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        fondo.setBounds(0, 0, 500, 500);
        fondo.setVisible(true);
        add(fondo);

        // Logo
        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(100, 20, 300, 100);
        logo.setVisible(true);
        fondo.add(logo);

        descripcion = new JTextPane();
        descripcion.setText("     Te despiertas en un sofá, te levantas y miras a tu alrededor: estaba Messi hablando con Maradona compartiendo unos mates, Boris Johnson hablando por celular, Guido Kaczka sentado en una silla admirando la vista y la Roca acariciando a un carpincho.");
        descripcion.setForeground(Color.WHITE);
        descripcion.setBounds(20,140,460,275);
        descripcion.setFont(new Font("Serif", Font.BOLD, 17));
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setVisible(true);
        fondo.add(descripcion);

        // Atras
        atras.setOpaque(false);
        atras.setContentAreaFilled(false);
        atras.setBorderPainted(false);
        atras.setBounds(20, 410, 50, 50);
        atras.setVisible(true);
        atras.setEnabled(false);
        fondo.add(atras);
        atras.addActionListener((ActionEvent e) -> {
            determinarDescripcion(false);
        });

        // Adelante
        adelante.setOpaque(false);
        adelante.setContentAreaFilled(false);
        adelante.setBorderPainted(false);
        adelante.setBounds(430, 410, 50, 50);
        adelante.setVisible(true);
        adelante.setEnabled(true);
        fondo.add(adelante);
        adelante.addActionListener((ActionEvent e) -> {
            determinarDescripcion(true);
        });

        // Salir
        salir.setBounds(140, 410, 220, 50);
        salir.setVisible(false);
        salir.setOpaque(false);
        salir.setContentAreaFilled(false);
        salir.setBorderPainted(false);
        fondo.add(salir);
        salir.addActionListener((ActionEvent e) -> {
            setVisible(false);
            menu.setVisible(true);
            dispose();
        });
  }

  private void determinarDescripcion(boolean siguiente) {
        ArrayList<String> parrafo = new ArrayList<>();
        parrafo.add("     Te despiertas en un sofá, te levantas y miras a tu alrededor: estaba Messi hablando con Maradona compartiendo unos mates, Boris Johnson hablando por celular, Guido Kaczka sentado en una silla admirando la vista y la Roca acariciando a un carpincho.");
        parrafo.add("     Intentas recordar qué pasó, pero nada se te viene a la mente. Boris deja el celular en la mesa y viene a hablar con vos. En su mejor esfuerzo de hablar en Español, dice que están en su quinta privada secreta en Mendoza: están perseguidos por fuerzas nazis en su último intento de regreso al poder, y ese es su único lugar de escondite. Por suerte, los nazis ya están casi derrotados. Las fuerzas armadas vienen al rescate, pero tardaran 12 horas en llegar. Afortunadamente, Zelenski está volando los alrededores en un avión de emergencia de la residencia de Boris, para protegerlos ante cualquier ataque enemigo.");
        parrafo.add("     Al término de la explicación le preguntas: ¿Cómo esto puede ser verdad? ¿Cómo carajo sabes español? ¿Y no era que Maradona se la dio contra el canterito? Maradona se levanta y te dice: “Demasiadas preguntas. Acá todos sabemos hablar en Español, excepto Boris que no se le da muy bien. Si eso te parece extraño, esperá a que el carpincho te gane al truco.” Te quedás helado, quieto. Maradona te grita: “¿No vas a venir a jugar? ¿Tenés miedo?” Así que te sientas en la silla y te preparas a jugar unas rondas de truco...");

        for(int i=0;i<parrafo.size();i++){
              if(descripcion.getText().equals(parrafo.get(i))){
                    atras.setEnabled(true);
                    adelante.setEnabled(true);

                    if(siguiente && i!=parrafo.size()-1){
                          descripcion.setText(parrafo.get(i+1));
                          if(i==1){
                                adelante.setEnabled(false);
                                salir.setVisible(true);
                          }
                    }
                    if(!siguiente && i!=0){
                          descripcion.setText(parrafo.get(i-1));
                          if(i==parrafo.size()-2)
                                atras.setEnabled(false);
                    }

                    break;
              }
        }
  }

}
