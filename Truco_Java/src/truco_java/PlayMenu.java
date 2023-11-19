package truco_java;

import java.awt.Color;
import java.awt.Font;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayMenu extends JFrame{

  public static int playerNumber=0;
  private static final Music effects = new Music();
  private JButton back;

  public PlayMenu (Truco_Java menu) throws IOException{
        setLayout(null);
        setDefaultCloseOperation(3);
        menu.setVisible(false);

        JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondo_juego.png")).getScaledInstance(500, 500, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 500, 500);
        background.setVisible(true);
        add(background);

        JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/logo.png")).getScaledInstance(300, 100, Image.SCALE_SMOOTH)));
        logo.setBounds(90, 10, 300, 100);
        logo.setVisible(true);
        background.add(logo);

        back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
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
            menu.setVisible(true);
            setVisible(false);
            dispose();
        });

        PlayMenu menuJugar = this;
        JTextField clientIp = new JTextField();
        JTextField clientPort = new JTextField();
        Action connectAction = new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    // Checks if the IP and the port are correctly formatted
                    if(clientIp.getText().isEmpty() || !clientIp.getText().matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}")){
                          JOptionPane.showMessageDialog(null, "Ha ingresado una dirección IP invalida");
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                          return;
                    }
                    if(clientPort.getText().isEmpty() || !clientPort.getText().matches("^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$")){
                          JOptionPane.showMessageDialog(null, "Ha ingresado un puerto invalido");
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                          return;
                    }

                    setVisible(false);
                    ClientMultiplayer game;
                    try {
                          game = new ClientMultiplayer(menu, clientIp.getText(), Integer.parseInt(clientPort.getText()), menuJugar);
                          game.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                          game.setResizable(false);
                          game.setTitle("Juego Truco - Cliente");
                          game.setBounds(0,0,505,800);
                          game.setLocationRelativeTo(null);
                          game.setVisible(true);
                    } catch (Exception ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                          setVisible(true);
                    }
              }
        };

        JLabel clientIpLabel = new JLabel("Direccion IP");
        clientIpLabel.setBounds(80, 235, 90, 15);
        clientIpLabel.setFont(new Font("Arial", Font.BOLD, 14));
        clientIpLabel.setForeground(Color.WHITE);
        clientIpLabel.setVisible(true);
        background.add(clientIpLabel);
        clientIp.setBounds(20,250,200,30);
        clientIp.setOpaque(false);
        clientIp.setBorder(null);
        clientIp.setHorizontalAlignment(JTextField.CENTER);
        clientIp.setForeground(Color.white);
        clientIp.setFont(new Font("Arial", Font.BOLD, 16));
        background.add(clientIp);
        JLabel ipBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(clientIp.getWidth(), clientIp.getHeight(), Image.SCALE_SMOOTH)));
        ipBackground.setBounds(clientIp.getX(),clientIp.getY(),clientIp.getWidth(),clientIp.getHeight());
        ipBackground.setVisible(true);
        background.add(ipBackground);
        // Enable only dot and numbers
        ((AbstractDocument)clientIp.getDocument()).setDocumentFilter(new DocumentFilter(){
              Pattern regEx = Pattern.compile("\\d*|\\.");
              @Override
              public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                  Matcher matcher = regEx.matcher(text);
                  if(!matcher.matches()){
                      return;
                  }
                  super.replace(fb, offset, length, text, attrs);
              }
          });

        JLabel clientPortLabel = new JLabel("Puerto");
        clientPortLabel.setBounds(95, 285, 90, 15);
        clientPortLabel.setFont(new Font("Arial", Font.BOLD, 15));
        clientPortLabel.setForeground(Color.WHITE);
        clientPortLabel.setVisible(true);
        background.add(clientPortLabel);
        clientPort.setBounds(20,300,200,30);
        clientPort.setText("1234");
        clientPort.setOpaque(false);
        clientPort.setBorder(null);
        clientPort.setHorizontalAlignment(JTextField.CENTER);
        clientPort.setForeground(Color.white);
        clientPort.setFont(new Font("Arial", Font.BOLD, 16));
        background.add(clientPort);
        JLabel clientPortBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(clientPort.getWidth(), clientPort.getHeight(), Image.SCALE_SMOOTH)));
        clientPortBackground.setBounds(clientPort.getX(),clientPort.getY(),clientPort.getWidth(),clientPort.getHeight());
        clientPortBackground.setVisible(true);
        background.add(clientPortBackground);
        clientIp.addActionListener((ActionEvent e) -> {
              clientPort.requestFocus();

        });
        clientPort.addActionListener(connectAction);
        // Enable only numbers
        ((AbstractDocument)clientPort.getDocument()).setDocumentFilter(new DocumentFilter(){
              Pattern regEx = Pattern.compile("\\d*");
              @Override
              public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                  Matcher matcher = regEx.matcher(text);
                  if(!matcher.matches()){
                      return;
                  }
                  super.replace(fb, offset, length, text, attrs);
              }
          });


        JButton connect = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/conectarBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        connect.setBounds(50, 340, 140, 40);
        connect.setVisible(true);
        connect.setOpaque(false);
        connect.setContentAreaFilled(false);
        connect.setBorderPainted(false);
        background.add(connect);
        connect.addActionListener(connectAction);

        JTextField serverPort = new JTextField();
        Action roomAction = new AbstractAction() {
              //Función encargada de analizar el presionado de tecla
              @Override
              public void actionPerformed(ActionEvent e) {
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    if(serverPort.getText().isEmpty() || !serverPort.getText().matches("^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$")){
                          JOptionPane.showMessageDialog(null, "Ha ingresado un puerto invalido");
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                          return;
                    }

                    setVisible(false);
                    ServerMultiplayer game;
                    try {
                          game = new ServerMultiplayer(menu, Integer.parseInt(serverPort.getText()), menuJugar);
                          game.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                          game.setResizable(false);
                          game.setTitle("Juego Truco - Servidor");
                          game.setBounds(0,0,505,800);
                          game.setLocationRelativeTo(null);
                          game.setVisible(false);
                    } catch (IOException ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                    }
              }
        };


        JLabel serverPortLabel = new JLabel("Puerto");
        serverPortLabel.setBounds(355, 260, 90, 15);
        serverPortLabel.setFont(new Font("Arial", Font.BOLD, 15));
        serverPortLabel.setForeground(Color.WHITE);
        serverPortLabel.setVisible(true);
        background.add(serverPortLabel);
        serverPort.setBounds(280,275,200,30);
        serverPort.setText("1234");
        serverPort.setOpaque(false);
        serverPort.setBorder(null);
        serverPort.setHorizontalAlignment(JTextField.CENTER);
        serverPort.setForeground(Color.white);
        serverPort.setFont(new Font("Arial", Font.BOLD, 16));
        background.add(serverPort);
        JLabel portServerBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(serverPort.getWidth(), serverPort.getHeight(), Image.SCALE_SMOOTH)));
        portServerBackground.setBounds(serverPort.getX(),serverPort.getY(),serverPort.getWidth(),serverPort.getHeight());
        portServerBackground.setVisible(true);
        background.add(portServerBackground);
        serverPort.addActionListener(roomAction);

        JButton roomButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/crearSalaBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        roomButton.setBounds(310, 340, 140, 40);
        roomButton.setVisible(true);
        roomButton.setOpaque(false);
        roomButton.setContentAreaFilled(false);
        roomButton.setBorderPainted(false);
        background.add(roomButton);
        roomButton.addActionListener(roomAction);

        JButton quickGameButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugarBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        quickGameButton.setBounds(317, 420, 140, 40);
        quickGameButton.setVisible(true);
        quickGameButton.setOpaque(false);
        quickGameButton.setContentAreaFilled(false);
        quickGameButton.setBorderPainted(false);
        background.add(quickGameButton);
        quickGameButton.addActionListener((ActionEvent e) -> {
            effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
            effects.play();
            setVisible(false);

            SinglePlayer game = new SinglePlayer(menu, this);
            game.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
            game.setResizable(false);
            game.setTitle("Juego Truco");
            game.setBounds(0,0,505,800);
            game.setLocationRelativeTo(null);
            game.setVisible(true);
        });

        ArrayList<JLabel> playersBackground = new ArrayList<>();
        for(int i=0; i<6;i++){
              final int pos = i;
              JButton player = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/"+i+".png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
              player.setBounds(0, 0, 60, 60);
              player.setVisible(true);
              player.setOpaque(false);
              player.setContentAreaFilled(false);
              player.setBorderPainted(false);
              player.addActionListener((ActionEvent e) -> {
                    try {
                          playersBackground.get(playerNumber).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/backgroundInactive.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
                    } catch (Exception ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la imagen de fondo: " + ex.getMessage());
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                    }
                    try {
                          playersBackground.get(pos).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/backgroundActive.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar la imagen de fondo: " + ex.getMessage());
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                    }
                    playerNumber=pos;
              });
              JLabel playerBackground = new JLabel();
              playerBackground.setVisible(true);
              playerBackground.setBounds(player.getX(), player.getY(), player.getWidth(), player.getHeight());
              playerBackground.setBounds(12+(65*i)+(18*i), 115, 60, 60);
              playersBackground.add(playerBackground);
              background.add(playerBackground);
              playerBackground.add(player);
        }
        // Puts the inactive background
        for(int x=0;x<playersBackground.size();x++)
              playersBackground.get(x).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/backgroundInactive.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        // Actives the first player (selected by default at creating the window)
        playersBackground.get(0).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/backgroundActive.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        playerNumber=0;
  }
}
