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
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class AcercaDe extends JFrame {
    private JTextPane description;
    private JButton back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private JButton next = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/adelante.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private JButton exit = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/volverBoton.png")).getScaledInstance(220, 50, Image.SCALE_SMOOTH)));
    private ArrayList<String> paragraph = new ArrayList<>();
    private static final Music efects = new Music();


  public AcercaDe (Truco_Java menu) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);

        paragraph.add("     Te despiertas en un sofá, te levantas y miras a tu alrededor: estaba Messi hablando con Maradona compartiendo unos mates, Boris Johnson hablando por celular, Guido Kaczka sentado en una silla admirando la vista y la Roca acariciando a un carpincho.");
        paragraph.add("     Intentas recordar qué pasó, pero nada se te viene a la mente. Boris deja el celular en la mesa y viene a hablar con vos. En su mejor esfuerzo de hablar en Español, dice que están en su quinta privada secreta en Mendoza: están perseguidos por fuerzas nazis en su último intento de regreso al poder, y este es su único lugar de escondite. Por suerte, los nazis ya están casi derrotados. Las fuerzas armadas vienen al rescate, pero tardaran 12 horas en llegar. Afortunadamente, Zelenski está volando los alrededores en un avión de emergencia de la residencia de Boris, para protegernos ante cualquier ataque enemigo.");
        paragraph.add("     Al término de la explicación le preguntas: ¿Cómo esto puede ser verdad? ¿Cómo carajo sabés español? ¿Y no era que Maradona se la dio contra el canterito? Maradona se levanta y te dice: “Demasiadas preguntas. Acá todos sabemos hablar en Español. Si eso te parece extraño, esperá a que el carpincho te gane al truco.” Te quedás helado, quieto. Maradona te grita: “¿No vas a venir a jugar? ¿Tenés miedo?” Así que te sientas en la silla y te preparas a jugar unas rondas de truco...");

        JLabel backgound = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        backgound.setBounds(0, 0, 500, 500);
        backgound.setVisible(true);
        add(backgound);

        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(100, 20, 300, 100);
        logo.setVisible(true);
        backgound.add(logo);

        description = new JTextPane();
        description.setText("     Te despiertas en un sofá, te levantas y miras a tu alrededor: estaba Messi hablando con Maradona compartiendo unos mates, Boris Johnson hablando por celular, Guido Kaczka sentado en una silla admirando la vista y la Roca acariciando a un carpincho.");
        description.setForeground(Color.WHITE);
        description.setBounds(20,130,450,275);
        description.setFont(new Font("Serif", Font.BOLD, 18));
        description.setEditable(false);
        description.setOpaque(false);
        description.setVisible(true);
        JScrollPane scroll = new JScrollPane(description);
        scroll.setVisible(true);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setForeground(Color.WHITE);
        scroll.setBorder(null);
        scroll.setViewportBorder(null);
        scroll.setBounds(20,130,450,275);
        backgound.add(scroll);

        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setBounds(20, 410, 50, 50);
        back.setVisible(true);
        back.setEnabled(false);
        backgound.add(back);
        back.addActionListener((ActionEvent e) -> {
            efects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efects.play();
            updateDescription(false);
        });

        next.setOpaque(false);
        next.setContentAreaFilled(false);
        next.setBorderPainted(false);
        next.setBounds(430, 410, 50, 50);
        next.setVisible(true);
        next.setEnabled(true);
        backgound.add(next);
        next.addActionListener((ActionEvent e) -> {
            efects.setFile("src/truco_java/musica/tirarCarta.wav", 1);
            efects.play();
            updateDescription(true);
        });

        exit.setBounds(140, 410, 220, 50);
        exit.setVisible(false);
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        backgound.add(exit);
        exit.addActionListener((ActionEvent e) -> {
            efects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efects.play();
            setVisible(false);
            menu.setVisible(true);
            dispose();
        });
  }

  private void updateDescription(boolean next_paragraph) {
        int indexOfDescription = paragraph.indexOf(description.getText());

        if(next_paragraph){
              description.setText(paragraph.get(indexOfDescription+1));
              indexOfDescription++;
        } else {
              description.setText(paragraph.get(indexOfDescription-1));
              indexOfDescription--;
        }

        int indexOfLastParagraph = paragraph.size()-1;

        back.setEnabled(indexOfDescription==0 ? false : true);
        next.setEnabled(indexOfDescription==indexOfLastParagraph ? false : true);
        exit.setVisible(indexOfDescription==indexOfLastParagraph ? true : false);
  }
}
