package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Registrarse extends JFrame {
    JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music efectos = new Music();

  public Registrarse () throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);
        Usuario.cargarUsuarios();

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
            setVisible(false);
            dispose();
        });

        // Usuario
        JLabel usuariosText = new JLabel("Usuario:");
        usuariosText.setBounds(220, 140, 100, 10);
        usuariosText.setFont(new Font("Arial", Font.BOLD, 14));
        usuariosText.setForeground(Color.WHITE);
        usuariosText.setVisible(true);
        fondo.add(usuariosText);
        JTextField usuario = new JTextField();
        usuario.setBounds(50,150,400,30);
        fondo.add(usuario);

        // Contraseña
        JLabel contraseñaText = new JLabel("Contraseña:");
        contraseñaText.setBounds(210, 200, 100, 10);
        contraseñaText.setFont(new Font("Arial", Font.BOLD, 14));
        contraseñaText.setForeground(Color.WHITE);
        contraseñaText.setVisible(true);
        fondo.add(contraseñaText);
        JPasswordField contraseña = new JPasswordField();
        contraseña.setBounds(50,210,400,30);
        fondo.add(contraseña);
        
        // Boton de registrarse
        JButton registrarse = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/registrarseBoton.png")).getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        registrarse.setBounds(175, 250, 150, 50);
        registrarse.setVisible(true);
        registrarse.setOpaque(false);
        registrarse.setContentAreaFilled(false);
        registrarse.setBorderPainted(false);
        fondo.add(registrarse);
        registrarse.addActionListener((ActionEvent e) -> {
            for(int i=0;i<Truco_Java.listaUsuarios.size();i++){
                if(Truco_Java.listaUsuarios.get(i).getNombre().equals(usuario.getText())){
                    JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. Intente con otro");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    return;
                }                    
            }
            Usuario nuevo;
            try {
                nuevo = new Usuario(usuario.getText(),new Encriptacion().encriptar(Truco_Java.ganadasJugador+";sig;"+Truco_Java.ganadasJugador, new String(contraseña.getPassword())));
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                    JOptionPane.showMessageDialog(null, "No se puedo crear el usuario. Intente con otra contraseña.");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    return;
            }
            if(!nuevo.iniciarSesion(new String(contraseña.getPassword())))
                return;
            Truco_Java.listaUsuarios.add(nuevo);
            Truco_Java.listaUsuarios.get(Truco_Java.listaUsuarios.size()-1).guardarCambios(false);
            try {
                Truco_Java.sesionAccion(true, Truco_Java.listaUsuarios.size()-1);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error en el inicio de sesion tras la registración.");
                efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                efectos.play();
                return;
            }
            setVisible(false);
            dispose();
        });
  }
}
