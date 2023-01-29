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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuJugar extends JFrame{

  public static int numeroJugador=0;
  private static final Music efectos = new Music();
  JButton atras;
  Truco_Java menu;

  public MenuJugar (Truco_Java menu) throws IOException{
        this.menu = menu;
        setLayout(null);
        setDefaultCloseOperation(3);
        menu.setVisible(false);


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
        atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
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

        JLabel multijugador = new JLabel("Multijugador");
        multijugador.setBounds(198, 180, 150, 40);
        multijugador.setFont(new Font("Arial", Font.BOLD, 20));
        multijugador.setForeground(Color.WHITE);
        multijugador.setVisible(true);
        fondo.add(multijugador);


        MenuJugar temp = this;
        JTextField ipCliente = new JTextField();
        JTextField puertoCliente = new JTextField();
        Action conectarAction = new AbstractAction() {
              //Función encargada de analizar el presionado de tecla
              @Override
              public void actionPerformed(ActionEvent e) {
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    setVisible(false);
                    InterfazCliente juego;
                    // TODO: CHECKEAR QUE SE INGRESARAN BIEN LA IP Y EL PUERTO
                    try {
                          juego = new InterfazCliente(menu, ipCliente.getText(), Integer.parseInt(puertoCliente.getText()), temp);
                          juego.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                          juego.setResizable(false);
                          juego.setTitle("Juego Truco - Cliente");
                          juego.setBounds(0,0,505,800);
                          juego.setLocationRelativeTo(null);
                          juego.setVisible(true);
                    } catch (IOException ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          efectos.play();
                    }
              }
        };

        JLabel ipLabel = new JLabel("Direccion IP");
        ipLabel.setBounds(80, 215, 90, 15);
        ipLabel.setFont(new Font("Arial", Font.BOLD, 15));
        ipLabel.setForeground(Color.WHITE);
        ipLabel.setVisible(true);
        fondo.add(ipLabel);
        ipCliente.setBounds(20,230,200,30);
        ipCliente.setOpaque(false);
        ipCliente.setBorder(null);
        ipCliente.setHorizontalAlignment(JTextField.CENTER);
        ipCliente.setForeground(Color.white);
        ipCliente.setFont(new Font("Arial", Font.BOLD, 16));
        fondo.add(ipCliente);
        JLabel fondoIp = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(ipCliente.getWidth(), ipCliente.getHeight(), Image.SCALE_SMOOTH)));
        fondoIp.setBounds(ipCliente.getX(),ipCliente.getY(),ipCliente.getWidth(),ipCliente.getHeight());
        fondoIp.setVisible(true);
        fondo.add(fondoIp);

        JLabel puertoClienteLabel = new JLabel("Puerto");
        puertoClienteLabel.setBounds(95, 265, 90, 15);
        puertoClienteLabel.setFont(new Font("Arial", Font.BOLD, 15));
        puertoClienteLabel.setForeground(Color.WHITE);
        puertoClienteLabel.setVisible(true);
        fondo.add(puertoClienteLabel);
        puertoCliente.setBounds(20,280,200,30);
        puertoCliente.setText("1234");
        puertoCliente.setOpaque(false);
        puertoCliente.setBorder(null);
        puertoCliente.setHorizontalAlignment(JTextField.CENTER);
        puertoCliente.setForeground(Color.white);
        puertoCliente.setFont(new Font("Arial", Font.BOLD, 16));
        fondo.add(puertoCliente);
        JLabel fondoPuertoCliente = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(puertoCliente.getWidth(), puertoCliente.getHeight(), Image.SCALE_SMOOTH)));
        fondoPuertoCliente.setBounds(puertoCliente.getX(),puertoCliente.getY(),puertoCliente.getWidth(),puertoCliente.getHeight());
        fondoPuertoCliente.setVisible(true);
        fondo.add(fondoPuertoCliente);
        ipCliente.addActionListener((ActionEvent e) -> {
              puertoCliente.requestFocus();

        });
        puertoCliente.addActionListener(conectarAction);


        JButton conectar = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/conectarBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        conectar.setBounds(50, 325, 140, 40);
        conectar.setVisible(true);
        conectar.setOpaque(false);
        conectar.setContentAreaFilled(false);
        conectar.setBorderPainted(false);
        fondo.add(conectar);
        conectar.addActionListener(conectarAction);

        JTextField puertoServer = new JTextField();
        Action salaAction = new AbstractAction() {
              //Función encargada de analizar el presionado de tecla
              @Override
              public void actionPerformed(ActionEvent e) {
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    setVisible(false);
                    InterfazServidor juego;
                    // TODO: CHECKEAR QUE SE INGRESARAN BIEN LA IP Y EL PUERTO
                    try {
                          juego = new InterfazServidor(menu, "localhost", Integer.parseInt(puertoServer.getText()), temp);
                          juego.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                          juego.setResizable(false);
                          juego.setTitle("Juego Truco - Servidor");
                          juego.setBounds(0,0,505,800);
                          juego.setLocationRelativeTo(null);
                          juego.setVisible(false);
                    } catch (IOException ex) {
                          JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                          efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          efectos.play();
                    }
              }
        };


        JLabel puertoServerLabel = new JLabel("Puerto");
        puertoServerLabel.setBounds(355, 245, 90, 15);
        puertoServerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        puertoServerLabel.setForeground(Color.WHITE);
        puertoServerLabel.setVisible(true);
        fondo.add(puertoServerLabel);
        puertoServer.setBounds(280,260,200,30);
        puertoServer.setText("1234");
        puertoServer.setOpaque(false);
        puertoServer.setBorder(null);
        puertoServer.setHorizontalAlignment(JTextField.CENTER);
        puertoServer.setForeground(Color.white);
        puertoServer.setFont(new Font("Arial", Font.BOLD, 16));
        fondo.add(puertoServer);
        JLabel fondoPuertoServer = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(puertoServer.getWidth(), puertoServer.getHeight(), Image.SCALE_SMOOTH)));
        fondoPuertoServer.setBounds(puertoServer.getX(),puertoServer.getY(),puertoServer.getWidth(),puertoServer.getHeight());
        fondoPuertoServer.setVisible(true);
        fondo.add(fondoPuertoServer);
        puertoServer.addActionListener(salaAction);

        JButton sala = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/crearSalaBoton.png")).getScaledInstance(140, 40, Image.SCALE_SMOOTH)));
        sala.setBounds(317, 325, 140, 40);
        sala.setVisible(true);
        sala.setOpaque(false);
        sala.setContentAreaFilled(false);
        sala.setBorderPainted(false);
        fondo.add(sala);
        sala.addActionListener(salaAction);

        JLabel partidaRapidaLabel = new JLabel("Partida Rapida");
        partidaRapidaLabel.setBounds(187, 370, 150, 40);
        partidaRapidaLabel.setFont(new Font("Arial", Font.BOLD, 20));
        partidaRapidaLabel.setForeground(Color.WHITE);
        partidaRapidaLabel.setVisible(true);
        fondo.add(partidaRapidaLabel);
        JButton partidaRapida = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugarBoton.png")).getScaledInstance(300, 50, Image.SCALE_SMOOTH)));
        partidaRapida.setBounds(110, 410, 300, 50);
        partidaRapida.setVisible(true);
        partidaRapida.setOpaque(false);
        partidaRapida.setContentAreaFilled(false);
        partidaRapida.setBorderPainted(false);
        fondo.add(partidaRapida);
        partidaRapida.addActionListener((ActionEvent e) -> {
            efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
            efectos.play();
            setVisible(false);
            InterfazJuego juego;
            try {
                juego = new InterfazJuego(menu);
                juego.setIconImage(new ImageIcon("src/truco_java/fondos/icono.png").getImage());
                juego.setResizable(false);
                juego.setTitle("Juego Truco");
                juego.setBounds(0,0,505,800);
                juego.setLocationRelativeTo(null);
                juego.setVisible(true);
                //Muestra el mensaje que avisa para comenzar el juego
                JOptionPane.showMessageDialog(null, "Aprete el mazo de cartas para comenzar el juego...");
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Ha sucedido un error al cargar el juego: " + ex.getMessage());
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
            }
        });



        ArrayList<JLabel> fondos = new ArrayList<>();
        for(int i=0; i<6;i++){
              final int pos = i;
              JButton jugador = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/"+i+".png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
              jugador.setBounds(0, 0, 60, 60);
              jugador.setVisible(true);
              jugador.setOpaque(false);
              jugador.setContentAreaFilled(false);
              jugador.setBorderPainted(false);
              jugador.addActionListener((ActionEvent e) -> {
                    for(int x=0;x<fondos.size();x++){
                          try {
                                fondos.get(x).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/fondoInactivo.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
                          } catch (Exception ex) {
                                System.out.println("NO SE ENCUENTRA LA IMAGEN DE FONDO");
                          }
                    }
                    try {
                          fondos.get(pos).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/fondo.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
                    } catch (Exception ex) {
                          System.out.println("NO SE ENCUENTRA LA IMAGEN DE FONDO");
                    }
                    numeroJugador=pos;
              });
              JLabel fondoJugador = new JLabel();
              fondoJugador.setVisible(true);
              fondoJugador.setBounds(jugador.getX(), jugador.getY(), jugador.getWidth(), jugador.getHeight());
              fondoJugador.setBounds(18+(65*i)+(18*i), 115, 60, 60);
              fondos.add(fondoJugador);
              fondo.add(fondoJugador);
              fondoJugador.add(jugador);
              for(int x=0;x<fondos.size();x++)
                    fondos.get(x).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/fondoInactivo.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
              fondos.get(0).setIcon(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/jugadores/fondo.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        }

        // Barra 1
        JLabel barra1 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/division.png")).getScaledInstance(188, 4, Image.SCALE_SMOOTH)));
        barra1.setBounds(5, 200, 188, 4);
        barra1.setVisible(true);
        fondo.add(barra1);
        // Barra 2
        JLabel barra2 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/division.png")).getScaledInstance(168, 4, Image.SCALE_SMOOTH)));
        barra2.setBounds(325, 200, 168, 4);
        barra2.setVisible(true);
        fondo.add(barra2);
        // Barra 3
        JLabel barra3 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/division.png")).getScaledInstance(178, 4, Image.SCALE_SMOOTH)));
        barra3.setBounds(5, 388, 178, 4);
        barra3.setVisible(true);
        fondo.add(barra3);
        // Barra 4
        JLabel barra4 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/division.png")).getScaledInstance(165, 4, Image.SCALE_SMOOTH)));
        barra4.setBounds(330, 388, 165, 4);
        barra4.setVisible(true);
        fondo.add(barra4);
  }
}
