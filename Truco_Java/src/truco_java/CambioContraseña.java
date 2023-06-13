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
    private JButton back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music effects = new Music();

  public CambioContraseña (Truco_Java menu) throws IOException {
        setLayout(null);
        setDefaultCloseOperation(3);
        menu.setVisible(false);

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
            menu.setVisible(true);
            setVisible(false);
            dispose();
        });

        JPasswordField password = new JPasswordField();
        JPasswordField password2 = new JPasswordField();
        Action startAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  if(!Arrays.equals(password.getPassword(), password2.getPassword())){
                          JOptionPane.showMessageDialog(null, "Las Contraseñas Ingresadas no son iguales.");
                          effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                          effects.play();
                          return;
                  }
                  Truco_Java.userList.get(Truco_Java.userIndex).setPassword(new String(password2.getPassword()));
                  Truco_Java.userList.get(Truco_Java.userIndex).encryptPoints();
                  Truco_Java.userList.get(Truco_Java.userIndex).saveChanges(false, 0);
                  menu.setVisible(true);
                  dispose();
            }
        };

        JLabel contraseñaText = new JLabel("Contraseña:");
        contraseñaText.setBounds(192, 135, 220, 15);
        contraseñaText.setFont(new Font("Arial", Font.BOLD, 19));
        contraseñaText.setForeground(Color.WHITE);
        contraseñaText.setVisible(true);
        background.add(contraseñaText);
        password.setBounds(50,155,400,30);
        password.setOpaque(false);
        password.setBorder(null);
        password.setHorizontalAlignment(JTextField.CENTER);
        password.setForeground(Color.white);
        password.setFont(new Font("Arial", Font.BOLD, 16));
        password.addActionListener((ActionEvent e) -> {
            password2.requestFocus();
        });
        background.add(password);
        JLabel passwordBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        passwordBackground.setBounds(50,155,400,30);
        passwordBackground.setVisible(true);
        background.add(passwordBackground);

        JLabel contraseñaText2 = new JLabel("Reitere:");
        contraseñaText2.setBounds(213, 195, 220, 15);
        contraseñaText2.setFont(new Font("Arial", Font.BOLD, 19));
        contraseñaText2.setForeground(Color.WHITE);
        contraseñaText2.setVisible(true);
        background.add(contraseñaText2);
        password2.setBounds(50,215,400,30);
        password2.setOpaque(false);
        password2.setBorder(null);
        password2.setHorizontalAlignment(JTextField.CENTER);
        password2.setForeground(Color.white);
        password2.setFont(new Font("Arial", Font.BOLD, 16));
        password2.addActionListener(startAction);
        background.add(password2);
        JLabel password2Background = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        password2Background.setBounds(50,215,400,30);
        password2Background.setVisible(true);
        background.add(password2Background);

        JButton submitButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/cambiarBoton.png")).getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        submitButton.setBounds(175, 255, 150, 50);
        submitButton.setVisible(true);
        submitButton.setOpaque(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setBorderPainted(false);
        background.add(submitButton);
        submitButton.addActionListener(startAction);
  }
}
