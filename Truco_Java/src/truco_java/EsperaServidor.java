package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class EsperaServidor extends JFrame {
    JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music efectos = new Music();

  public EsperaServidor (MenuJugar menu, int puertoNum) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);

        // Fondo
        JLabel fondo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        fondo.setBounds(0, 0, 500, 500);
        fondo.setVisible(true);
        add(fondo);

        // Logo
        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(90, 10, 300, 100);
        logo.setVisible(true);
        fondo.add(logo);

        // Atras
        atras.setOpaque(false);
        atras.setContentAreaFilled(false);
        atras.setBorderPainted(false);
        atras.setBounds(20, 20, 50, 50);
        atras.setVisible(true);
        atras.setEnabled(true);
        fondo.add(atras);
        atras.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            menu.setVisible(true);
            setVisible(false);
            dispose();
        });

        JLabel esperando = new JLabel("Esperando conexion");
        esperando.setBounds(130, 115, 250, 25);
        esperando.setFont(new Font("Arial", Font.BOLD, 25));
        esperando.setForeground(Color.WHITE);
        esperando.setVisible(true);
        fondo.add(esperando);

        JLabel direccion = new JLabel("La dirección de la sala es...");
        direccion.setBounds(140, 150, 250, 15);
        direccion.setFont(new Font("Arial", Font.BOLD, 18));
        direccion.setForeground(Color.WHITE);
        direccion.setVisible(true);
        fondo.add(direccion);

        JLabel ip = new JLabel("IP:");
        ip.setBounds(90, 200, 50, 15);
        ip.setFont(new Font("Arial", Font.BOLD, 15));
        ip.setForeground(Color.WHITE);
        ip.setVisible(true);
        fondo.add(ip);

        JLabel puerto = new JLabel("Puerto:");
        puerto.setBounds(90, 230, 100, 15);
        puerto.setFont(new Font("Arial", Font.BOLD, 15));
        puerto.setForeground(Color.WHITE);
        puerto.setVisible(true);
        fondo.add(puerto);

        JTextPane datos = new JTextPane();
        datos.setBounds(90,190,320,55);
        datos.setText(obtenerIPs()+"\n"+puertoNum);
        datos.setForeground(Color.WHITE);
        datos.setFont(new Font("Arial", Font.BOLD, 25));
        datos.setEditable(false);
        datos.setOpaque(false);
        datos.setVisible(true);
        fondo.add(datos);
        StyledDocument doc = datos.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
  }

  private String obtenerIPs(){
        Enumeration<NetworkInterface> n;
        ArrayList<String> IPs = new ArrayList<>();
        String plantillaIP = "192.168";
        try {
              n = NetworkInterface.getNetworkInterfaces();
              for (; n.hasMoreElements();) {
                    NetworkInterface e = n.nextElement();
                    Enumeration<InetAddress> a = e.getInetAddresses();
                    for (; a.hasMoreElements();) {
                          InetAddress addr = a.nextElement();
                          IPs.add(addr.getHostAddress());
                    }
              }
        } catch (Exception e) {
              return "localhost";
        }

        for (String i : IPs) {
            if(i.substring(0,7).equals(plantillaIP))
                  return i;
        }
        return "localhost";
  }
}
