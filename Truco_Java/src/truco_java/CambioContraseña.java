package truco_java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CambioContraseña extends JFrame {
    JButton atras = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music efectos = new Music();

  public CambioContraseña (Truco_Java menu) throws IOException {
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

        JPasswordField contraseña = new JPasswordField();
        JPasswordField contraseña2 = new JPasswordField();
        Action iniciarAccion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(!Arrays. equals(contraseña.getPassword(),contraseña2.getPassword())){
                    JOptionPane.showMessageDialog(null, "Las Contraseñas Ingresadas no son iguales.");
                    efectos.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    efectos.play();
                    return;
            }
            Truco_Java.listaUsuarios.get(Truco_Java.posUsuario).setContraseña(new String(contraseña2.getPassword()));
            Truco_Java.listaUsuarios.get(Truco_Java.posUsuario).encriptaPuntaje();
            Truco_Java.listaUsuarios.get(Truco_Java.posUsuario).guardarCambios(false, 0);
            menu.setVisible(true);
            dispose();
            }
        };

        // Contraseña 1
        JLabel contraseñaText = new JLabel("Contraseña:");
        contraseñaText.setBounds(192, 135, 120, 15);
        contraseñaText.setFont(new Font("Arial", Font.BOLD, 19));
        contraseñaText.setForeground(Color.WHITE);
        contraseñaText.setVisible(true);
        fondo.add(contraseñaText);
        contraseña.setBounds(50,155,400,30);
        contraseña.setOpaque(false);
        contraseña.setBorder(null);
        contraseña.setHorizontalAlignment(JTextField.CENTER);
        contraseña.setForeground(Color.white);
        contraseña.setFont(new Font("Arial", Font.BOLD, 16));
        contraseña.addActionListener((ActionEvent e) -> {
            contraseña2.requestFocus();
        });
        fondo.add(contraseña);
        // Fondo para mejorar la apariencia
        JLabel fondoContraseña = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        fondoContraseña.setBounds(50,155,400,30);
        fondoContraseña.setVisible(true);
        fondo.add(fondoContraseña);

        // Contraseña 2
        JLabel contraseñaText2 = new JLabel("Reitere:");
        contraseñaText2.setBounds(213, 195, 120, 15);
        contraseñaText2.setFont(new Font("Arial", Font.BOLD, 19));
        contraseñaText2.setForeground(Color.WHITE);
        contraseñaText2.setVisible(true);
        fondo.add(contraseñaText2);
        contraseña2.setBounds(50,215,400,30);
        contraseña2.setOpaque(false);
        contraseña2.setBorder(null);
        contraseña2.setHorizontalAlignment(JTextField.CENTER);
        contraseña2.setForeground(Color.white);
        contraseña2.setFont(new Font("Arial", Font.BOLD, 16));
        contraseña2.addActionListener(iniciarAccion);
        fondo.add(contraseña2);
        // Fondo para mejorar la apariencia
        JLabel fondoContraseña2 = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        fondoContraseña2.setBounds(50,215,400,30);
        fondoContraseña2.setVisible(true);
        fondo.add(fondoContraseña2);

        // Boton de registrarse
        JButton registrarse = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cambiarBoton.png")).getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        registrarse.setBounds(175, 255, 150, 50);
        registrarse.setVisible(true);
        registrarse.setOpaque(false);
        registrarse.setContentAreaFilled(false);
        registrarse.setBorderPainted(false);
        fondo.add(registrarse);
        registrarse.addActionListener((ActionEvent e) -> {
        });
  }
}
