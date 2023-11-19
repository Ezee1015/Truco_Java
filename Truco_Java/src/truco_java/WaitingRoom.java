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

public class WaitingRoom extends JFrame {
    private JButton back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music effects = new Music();
    private boolean local = true;
    private String publicIP;

  public WaitingRoom (PlayMenu menu, int port, ServerMultiplayer serverInterface) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);

        JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_acerca.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 500, 500);
        background.setVisible(true);
        add(background);

        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(90, 10, 300, 100);
        logo.setVisible(true);
        background.add(logo);

        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setBounds(20, 20, 50, 50);
        back.setVisible(true);
        back.setEnabled(true);
        background.add(back);
        back.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();

            try {
                  // Closes the server
                  new Client("127.0.0.1", port);
                  new Client("127.0.0.1", port+1);
                  while(serverInterface.server==null);
                  while(serverInterface.chat_socket==null);
                  serverInterface.server.killServer();
                  serverInterface.chat_socket.killServer();

                  // Closes the windows and shows the menu
                  menu.setVisible(true);
                  setVisible(false);
                  dispose();
            } catch (Exception ex) {
                  JOptionPane.showMessageDialog(null, "Ha sucedido un error al cerrar la conexión: " + ex.getMessage());
                  effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                  effects.play();
            }
        });

        JLabel waitLabel = new JLabel("Esperando conexion");
        waitLabel.setBounds(130, 115, 250, 25);
        waitLabel.setFont(new Font("Arial", Font.BOLD, 25));
        waitLabel.setForeground(Color.WHITE);
        waitLabel.setVisible(true);
        background.add(waitLabel);

        JLabel directionLabel = new JLabel("La dirección de la sala es...");
        directionLabel.setBounds(140, 150, 250, 15);
        directionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        directionLabel.setForeground(Color.WHITE);
        directionLabel.setVisible(true);
        background.add(directionLabel);

        JLabel ipLabel = new JLabel("IP:");
        ipLabel.setBounds(90, 200, 150, 15);
        ipLabel.setFont(new Font("Arial", Font.BOLD, 15));
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setVisible(true);
        background.add(ipLabel);

        JLabel portLabel = new JLabel("Puerto:");
        portLabel.setBounds(90, 230, 100, 15);
        portLabel.setFont(new Font("Arial", Font.BOLD, 15));
        portLabel.setForeground(Color.WHITE);
        portLabel.setVisible(true);
        background.add(portLabel);

        JTextPane data = new JTextPane();
        data.setBounds(100,180,320,65);
        data.setText(localIP()+"\n"+port);
        data.setForeground(Color.WHITE);
        data.setFont(new Font("Arial", Font.BOLD, 25));
        data.setEditable(false);
        data.setOpaque(false);
        data.setVisible(true);
        background.add(data);
        StyledDocument doc = data.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JLabel internetWarning = new JLabel("Debe configurar 'Port Forwarding' en el router para aceptar conexiones entrantes");
        internetWarning.setBounds(10, 250, 500, 20);
        internetWarning.setFont(new Font("Arial", Font.BOLD, 12));
        internetWarning.setForeground(Color.WHITE);
        internetWarning.setVisible(false);
        background.add(internetWarning);

        JButton ipSwitchButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/localtrue.png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
        ipSwitchButton.setBounds(400, 200, 75, 40);
        ipSwitchButton.setVisible(true);
        ipSwitchButton.setOpaque(false);
        ipSwitchButton.setContentAreaFilled(false);
        ipSwitchButton.setBorderPainted(false);
        background.add(ipSwitchButton);
        ipSwitchButton.addActionListener((ActionEvent e) -> {
              if(publicIP==null)
                     publicIP = publicIP();
              try {
                    if(local){
                          data.setText(publicIP+"\n"+port);
                          ipLabel.setText("IP Pública:");
                          internetWarning.setVisible(true);
                    }else{
                          data.setText(localIP()+"\n"+port);
                          ipLabel.setText("IP:");
                          internetWarning.setVisible(false);
                    }

                    local=!local;
                    try {
                          ipSwitchButton.setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/local"+local+".png")).getScaledInstance(75, 40, Image.SCALE_SMOOTH)));
                    } catch (Exception ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de dibujar el icono de la IP: " + ex.getMessage());
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                    }
              } catch (Exception ex) {
                  JOptionPane.showMessageDialog(null, "Ha sucedido un error al momento de obtener la IP Pública: " + ex.getMessage());
                  effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                  effects.play();
              }

        });
  }

  private String localIP(){
        Enumeration<NetworkInterface> n;
        ArrayList<String> IPs = new ArrayList<>();
        String IPTemplate = "192.168";
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
              if(i.substring(0,7).equals(IPTemplate))
                    return i;
        }
        return "127.0.0.1";
  }

  private static String publicIP(){
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
