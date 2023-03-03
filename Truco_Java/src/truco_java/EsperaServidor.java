package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class EsperaServidor extends JFrame {
    JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music efectos = new Music();
    private boolean local = true;
    private String publicIP;

  public EsperaServidor (MenuJugar menu, int puertoNum, InterfazServidor servidor) throws IOException {
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

            try {
                  // Cierra el servidor
                  new Cliente("127.0.0.1", puertoNum);
                  while(servidor.server==null);
                  servidor.server.killServer();

                  // Cierra la ventana y aparece el menu
                  menu.setVisible(true);
                  setVisible(false);
                  dispose();
            } catch (Exception ex) {
                  JOptionPane.showMessageDialog(null, "Ha sucedido un error al cerrar la conexión: " + ex.getMessage());
                  efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                  efectos.play();
            }
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
        ip.setBounds(90, 200, 150, 15);
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
        datos.setBounds(100,180,320,65);
        datos.setText(IPLocal()+"\n"+puertoNum);
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

        JLabel advertencia = new JLabel("Debe configurar 'Port Forwarding' en el router para aceptar conexiones entrantes");
        advertencia.setBounds(10, 250, 500, 20);
        advertencia.setFont(new Font("Arial", Font.BOLD, 12));
        advertencia.setForeground(Color.WHITE);
        advertencia.setVisible(false);
        fondo.add(advertencia);

        JButton ipSwitch = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/localtrue.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        ipSwitch.setBounds(400, 200, 75, 40);
        ipSwitch.setVisible(true);
        ipSwitch.setOpaque(false);
        ipSwitch.setContentAreaFilled(false);
        ipSwitch.setBorderPainted(false);
        fondo.add(ipSwitch);
        ipSwitch.addActionListener((ActionEvent e) -> {
              if(publicIP==null)
                     publicIP = IPPublica();
              try {
                    if(local){
                          datos.setText(publicIP+"\n"+puertoNum);
                          ip.setText("IP Pública:");
                          advertencia.setVisible(true);
                    }else{
                          datos.setText(IPLocal()+"\n"+puertoNum);
                          ip.setText("IP:");
                          advertencia.setVisible(false);
                    }

                    local=!local;
                    try {
                          ipSwitch.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/local"+local+".png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
                    } catch (Exception ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar el icono de la IP: " + ex.getMessage());
                          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          efectos.play();
                    }
              } catch (Exception ex) {
                  JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de obtener la IP Pública: " + ex.getMessage());
                  efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                  efectos.play();
              }

        });
  }

  private String IPLocal(){
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
              return "127.0.0.1";
        }

        for (String i : IPs) {
              if(i.substring(0,7).equals(plantillaIP))
                    return i;
        }
        return "127.0.0.1";
  }

  private static String IPPublica(){
        String urlString = "https://ifconfig.co/ip";
        try {
              URL url = new URL(urlString);
              try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    return br.readLine();
              }
        } catch (Exception e) {
              urlString = "https://icanhazip.com/";
              try {
                    URL url = new URL(urlString);
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                          return br.readLine();
                    }
              } catch (Exception ex) {
                    return "Desconocido";
              }
        }
  }
}
