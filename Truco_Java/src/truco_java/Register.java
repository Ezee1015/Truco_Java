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
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JFrame {
    private JButton back = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/atras.png")).getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
    private static final Music effects = new Music();

  public Register (Truco_Java menu) throws IOException {
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

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(210, 135, 220, 15);
        userLabel.setFont(new Font("Arial", Font.BOLD, 19));
        userLabel.setForeground(Color.WHITE);
        userLabel.setVisible(true);
        background.add(userLabel);
        JTextField userField = new JTextField();
        userField.setBounds(50,155,400,30);
        userField.setOpaque(false);
        userField.setBorder(null);
        userField.setHorizontalAlignment(JTextField.CENTER);
        userField.setForeground(Color.white);
        userField.setFont(new Font("Arial", Font.BOLD, 16));
        background.add(userField);
        JLabel userBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        userBackground.setBounds(50,155,400,30);
        userBackground.setVisible(true);
        background.add(userBackground);

        JPasswordField passwordField = new JPasswordField();
        userField.addActionListener((ActionEvent e) -> {
            passwordField.requestFocus();
        });

        Action registerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0;i<Truco_Java.userList.size();i++){
                    if(Truco_Java.userList.get(i).getName().equals(userField.getText())){
                        JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. Intente con otro");
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                        return;
                    }
                }
                User newUser;
                try {
                    newUser = new User(userField.getText(),new Encryption().encrypt(Truco_Java.gamesWonPlayer+";sig;"+Truco_Java.gamesWonAi, new String(passwordField.getPassword())));
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                        JOptionPane.showMessageDialog(null, "No se puedo crear el usuario. Intente con otra contraseña.");
                        effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                        effects.play();
                        return;
                }
                if(!newUser.logIn(new String(passwordField.getPassword())))
                    return;
                Truco_Java.userList.add(newUser);
                try {
                    Truco_Java.sessionAction(true, Truco_Java.userList.size()-1, false);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error en el inicio de sesion tras la registración.");
                    effects.setFile("src/truco_java/musica/botonMenu.wav", 1);
                    effects.play();
                    return;
                }
                Truco_Java.userList.get(Truco_Java.userIndex).saveChanges(false, 0);
                menu.setVisible(true);
                setVisible(false);
                dispose();
            }
        };

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(192, 195, 220, 15);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 19));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setVisible(true);
        background.add(passwordLabel);
        passwordField.setBounds(50,215,400,30);
        passwordField.setOpaque(false);
        passwordField.setBorder(null);
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setForeground(Color.white);
        passwordField.setFont(new Font("Arial", Font.BOLD, 16));
        passwordField.addActionListener(registerAction);
        background.add(passwordField);
        JLabel passwordBackground = new JLabel(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/fondoCheckBox.png")).getScaledInstance(400, 30, Image.SCALE_SMOOTH)));
        passwordBackground.setBounds(50, 215, 400, 30);
        passwordBackground.setVisible(true);
        background.add(passwordBackground);

        JButton registerButton = new JButton(new ImageIcon(ImageIO.read(new File("src/truco_java/fondos/registrarseBoton.png")).getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
        registerButton.setBounds(175, 255, 150, 50);
        registerButton.setVisible(true);
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        background.add(registerButton);
        registerButton.addActionListener(registerAction);
  }
}
